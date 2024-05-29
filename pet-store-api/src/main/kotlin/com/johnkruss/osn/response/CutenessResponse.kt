package com.johnkruss.osn.response

import com.johnkruss.osn.domain.Species

data class CutenessResponse(
    val species: Species,
    val names: List<String>,
    val totalCuteness: Int,
)
