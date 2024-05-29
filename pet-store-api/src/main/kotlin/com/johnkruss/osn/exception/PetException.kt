package com.johnkruss.osn.exception

data class PetException(
    val code: Int,
    override val message: String,
) : RuntimeException()
