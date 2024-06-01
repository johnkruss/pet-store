plugins {
    id("java")
    alias(libs.plugins.micronaut.minimal.application)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":pet-store-api"))
    implementation(project(":pet-store-dynamo-repository"))
    implementation(libs.logback)
    implementation(libs.logback.encoder)
    implementation(libs.snakeyaml)

    implementation(libs.micronaut.jackson)

    testImplementation(project(":pet-store-test-common"))
    testImplementation(libs.micronaut.http.client)
    testImplementation(libs.mockk)
}

tasks.named<JavaExec>("run") {
    environment("AWS_ACCESS_KEY_ID", "ACCESS_KEY")
    environment("AWS_SECRET_ACCESS_KEY", "SECRET_KEY")
    environment("AWS_SESSION_TOKEN", "TOKEN")
    environment("AWS_REGION", "us-east-1")
}

tasks.withType<Test> {
    environment("AWS_ACCESS_KEY_ID", "ACCESS_KEY")
    environment("AWS_SECRET_ACCESS_KEY", "SECRET_KEY")
    environment("AWS_SESSION_TOKEN", "TOKEN")
    environment("AWS_REGION", "us-east-1")
}

application {
    mainClass.set("com.johnkruss.osn.Application")
}

micronaut {
    version(libs.versions.micronaut.get())
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("com.johnkruss.*")
    }
}
