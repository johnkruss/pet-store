package com.johnkruss.osn.domain

data class Pet(
    var name: String = "",
    var species: Species = Species.CAPYBARA,
    var cuteness: Int? = 9001,
)
