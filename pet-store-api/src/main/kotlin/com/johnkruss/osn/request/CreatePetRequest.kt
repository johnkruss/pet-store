package com.johnkruss.osn.request

data class CreatePetRequest(
    var petName: String,
    val cuteness: Int,
)
