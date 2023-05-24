package com.chewy.osn.controller;

import com.chewy.osn.domain.Species;
import com.chewy.osn.request.CreatePetRequest;
import com.chewy.osn.response.CutenessResponse;
import com.chewy.osn.response.GetPetResponse;
import com.chewy.osn.service.PetService;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/v1/species/{species}")
public class PetController {

    private PetService petService;

    @Inject
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Get("/names/{name}")
    public GetPetResponse getPet(@PathVariable("species") Species species,
                                 @PathVariable("name") String name) {
        GetPetResponse response = petService.getPet(species, name);
        return response;
    }

    @Get("{?names}")
    public CutenessResponse getCuteness(@PathVariable("species") Species species,
                                        @QueryValue("names") List<String> names) {
        return petService.getTotalCuteness(species, names);
    }

    @Post
    public void createPet(@PathVariable("species") Species species,
                          @Body CreatePetRequest request) {
        petService.createPet(species, request.getPetName(), request.getCuteness());
    }
}
