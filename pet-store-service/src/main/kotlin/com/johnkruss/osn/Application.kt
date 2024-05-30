package com.johnkruss.osn

import io.micronaut.runtime.Micronaut

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build(*args)
            .eagerInitSingletons(true)
            .mainClass(Application::class.java)
            .start()
    }
}
