package com.chewy.osn.service

import com.chewy.osn.domain.Pet
import com.chewy.osn.domain.Species
import com.chewy.osn.exception.PetException
import com.chewy.osn.generatePet
import com.chewy.osn.repository.PetRepository
import com.chewy.osn.response.GetPetResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class PetServiceTest: FreeSpec ({

    lateinit var petRepository: PetRepository
    lateinit var petService: PetService

    beforeTest {
        petRepository = mockk()
        petService = PetService(petRepository)
    }

    "Search for and find a pet" {
        //setup
        val species = Species.BIRD
        val name = "Hootie"

        val pet = Pet(name, species, 42)

        every { petRepository.getPet(species, name) } returns pet

        //execution
        val returned = petService.getPet(species, name)

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, name) }
        //make sure no other mock interactions happen that we were not expecting
        confirmVerified(petRepository)

        returned shouldBe GetPetResponse(42)
    }

    "An exception is thrown when no pet is found!" {
        //setup
        val species = Species.DOG
        val name = "Toto"

        every { petRepository.getPet(species, name) } returns null

        //execution
        val thrownException = shouldThrow<PetException> {
            petService.getPet(species, name)
        }

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, name) }
        confirmVerified(petRepository)

        thrownException.code shouldBe 404
        thrownException.message shouldBe "Toto was already adopted! :D"
    }

    "calculate cuteness for some pets"() {
        //setup
        val pets = listOf(
            generatePet(cuteness = 123),
            generatePet(cuteness = 456)
        )

        //exection
        val result = petService.calculateCuteness(pets)

        //assertions
        result shouldBe 579
    }

    "Pets with null cuteness are ignored" {
        //setup
        val pets = listOf(
            generatePet(cuteness = 123),
            generatePet(cuteness = null)
        )

        //exection
        val result = petService.calculateCuteness(pets)

        //assertions
        result shouldBe 123
    }

    "Retrieve several pets, ignore nulls" {
        //setup
        val species = Species.CAT
        val name1 = "Simba"
        val name2 = "Garfield"
        val names = listOf(name1, name2)

        val pet1 = generatePet(name = name1, species = species, cuteness = 345)

        every { petRepository.getPet(species, name1) } returns pet1
        every { petRepository.getPet(species, name2) } returns null

        //execution
        val response = petService.getPets(species, names)

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, name1) }
        verify(exactly = 1) { petRepository.getPet(species, name2) }
        confirmVerified(petRepository)

        response.size shouldBe 1
        response.contains(pet1)
    }

    "Calculate the total cuteness" {
        //setup
        val species = Species.DOG
        val name1 = "Beethovan"
        val name2 = "Buddy"
        val names = listOf(name1, name2)

        val pet1 = generatePet(name = name1, species = species, cuteness = 345)
        val pet2 = generatePet(name = name2, species = species, cuteness = 654)

        every { petRepository.getPet(species, name1) } returns pet1
        every { petRepository.getPet(species, name2) } returns pet2

        //execution
        val response = petService.getTotalCuteness(species, names)

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, name1) }
        verify(exactly = 1) { petRepository.getPet(species, name2) }
        confirmVerified(petRepository)

        response.species shouldBe species
        response.names shouldBe names
        response.totalCuteness shouldBe 999
    }

    "Successfully create a pet" {
        //setup
        val petName = "Woodstock"
        val species = Species.BIRD
        val cuteness = 17

        val expectedPet = Pet(petName, species, cuteness)

        every { petRepository.getPet(species, petName) } returns null
        justRun { petRepository.writePet(expectedPet) }

        //execution
        petService.createPet(species, petName, cuteness)

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, petName) }
        verify(exactly = 1) { petRepository.writePet(expectedPet) }
        confirmVerified(petRepository)
    }

    "An exception is thrown when a pet already exists" {
        //setup
        val petName = "Woodstock"
        val species = Species.BIRD
        val cuteness = 17

        val existingPet = generatePet(name = petName, species = species, cuteness = cuteness)

        every { petRepository.getPet(species, petName) } returns existingPet

        //execution
        val thrownException = shouldThrow<PetException> {
            petService.createPet(species, petName, cuteness)
        }

        //assertions
        verify(exactly = 1) { petRepository.getPet(species, petName) }
        confirmVerified(petRepository)

        thrownException.code shouldBe 409
        thrownException.message shouldBe "We already have a BIRD that's named Woodstock"
    }
})