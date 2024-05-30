package com.johnkruss.osn.exception

data class HttpStatusException(
    val statusCode: Int,
    override val message: String,
) : RuntimeException() {
    constructor(e: Throwable) : this(500, e.message ?: "ERROR")
}
