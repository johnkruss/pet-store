package com.johnkruss.osn.service

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import com.johnkruss.osn.dynamo.PetRepository
import com.johnkruss.osn.exception.PetException
import com.johnkruss.osn.response.CutenessResponse
import com.johnkruss.osn.response.GetPetResponse
import jakarta.inject.Inject

class PetService
    @Inject
    constructor(private val petRepository: PetRepository) {
        fun getPet(
            species: Species,
            name: String,
        ): GetPetResponse {
            val pet = petRepository.getPet(species, name) ?: throw PetException(404, "$name was already adopted! :D")

            return GetPetResponse(pet.cuteness)
        }

        fun getTotalCuteness(
            species: Species,
            names: List<String>,
        ): CutenessResponse {
            val pets = getPets(species, names)
            val totalCuteness = calculateCuteness(pets)

            return CutenessResponse(species, names, totalCuteness)
        }

        fun calculateCuteness(cutePets: List<Pet>): Int {
            return cutePets.mapNotNull { it.cuteness }.sum()
        }

        fun getPets(
            species: Species,
            names: List<String>,
        ): List<Pet> {
            val pets: MutableList<Pet> = ArrayList()

            for (name in names) {
                val retrieved = petRepository.getPet(species, name)

                if (retrieved != null) {
                    pets.add(retrieved)
                }
            }
            return pets
        }

        fun createPet(
            species: Species,
            name: String,
            cuteness: Int,
        ) {
            val existingPet = petRepository.getPet(species, name)

            if (existingPet != null) {
                throw PetException(409, "We already have a $species that's named $name")
            }

            petRepository.writePet(Pet(name, species, cuteness))
        }
    }
