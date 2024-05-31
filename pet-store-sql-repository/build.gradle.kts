plugins {
    id("java-library")
}

dependencies {
    implementation(project(":pet-store-api"))
    api(libs.jooq)
    api(libs.postgres)
    api(libs.flyway)
    api(libs.flyway.postgres)

    testImplementation(project(":pet-store-test-common"))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.mockk)
    testImplementation(libs.hikari)
}