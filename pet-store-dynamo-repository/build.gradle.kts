plugins {
    id("java-library")
}

dependencies {
    implementation(project(":pet-store-api"))
    api(libs.dynamodb.enhanced)

    testImplementation(project(":pet-store-test-common"))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
}

tasks.withType<Test> {
    environment("AWS_ACCESS_KEY_ID", "ACCESS_KEY")
    environment("AWS_SECRET_ACCESS_KEY", "SECRET_KEY")
    environment("AWS_SESSION_TOKEN", "TOKEN")
}