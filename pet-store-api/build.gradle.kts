plugins {
    id("java-library")
}

dependencies {
    api(libs.jackson.databind)
    api(libs.jackson.core)
    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.datatype.jdk8)
    api(libs.jackson.module.kotlin)

    testImplementation(project(":pet-store-test-common"))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
}
