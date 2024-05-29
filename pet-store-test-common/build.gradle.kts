plugins {
    id("java")
}

dependencies {
    implementation(project(":pet-store-api"))
    implementation(libs.dynamodb.enhanced)
}