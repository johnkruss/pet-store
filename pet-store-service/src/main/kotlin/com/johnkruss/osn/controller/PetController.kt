package com.johnkruss.osn.controller

import com.johnkruss.osn.domain.Species
import com.johnkruss.osn.request.CreatePetRequest
import com.johnkruss.osn.response.CutenessResponse
import com.johnkruss.osn.response.GetPetResponse
import com.johnkruss.osn.service.PetService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import jakarta.inject.Inject

@Controller("/v1/species/{species}")
class PetController
    @Inject
    constructor(private val petService: PetService) {
        @Get("/names/{name}")
        fun getPet(
            @PathVariable("species") species: Species,
            @PathVariable("name") name: String,
        ): GetPetResponse {
            val response = petService.getPet(species, name)
            return response
        }

        @Get("/{names}")
        fun getCuteness(
            @PathVariable("species") species: Species,
            @QueryValue("names") names: List<String>,
        ): CutenessResponse {
            return petService.getTotalCuteness(species, names)
        }

        @Post
        fun createPet(
            @PathVariable("species") species: Species,
            @Body request: CreatePetRequest,
        ) {
            petService.createPet(species, request.petName, request.cuteness)
        }
    }
