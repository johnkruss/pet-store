package com.chewy.osn.service;

import com.chewy.osn.domain.Pet;
import com.chewy.osn.domain.Species;
import com.chewy.osn.exception.PetException;
import com.chewy.osn.repository.PetRepository;
import com.chewy.osn.response.CutenessResponse;
import com.chewy.osn.response.GetPetResponse;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetService {

    private PetRepository petRepository;

    @Inject
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public GetPetResponse getPet(Species species, String name) {
        Pet pet = petRepository.getPet(species, name);

        if (pet == null) {
            throw new PetException(404, name + " was already adopted! :D");
        }
        return new GetPetResponse(pet.getCuteness());
    }

    public CutenessResponse getTotalCuteness(Species species, List<String> names) {
        List<Pet> pets = getPets(species, names);
        Integer totalCuteness = calculateCuteness(pets);

        return new CutenessResponse(species, names, totalCuteness);
    }

    public Integer calculateCuteness(List<Pet> cutePets) {
        return cutePets.stream()
            .map(Pet::getCuteness)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue).sum();
    }

    public List<Pet> getPets(Species species, List<String> names) {
        List<Pet> pets = new ArrayList<>();

        for (String name: names) {
            Pet retrieved = petRepository.getPet(species, name);

            if (retrieved != null) {
                pets.add(retrieved);
            }
        }
        return pets;
    }

    public void createPet(Species species, String name, Integer cuteness) {
        Pet existingPet = petRepository.getPet(species, name);

        if (existingPet != null) {
            throw new PetException(409, "We already have a " + species + " that's named " + name);
        }

        petRepository.writePet(new Pet(name, species, cuteness));
    }
}
