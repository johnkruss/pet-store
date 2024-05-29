import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.diffplug.spotless")

    group = "com.johnkruss.osn"

    spotless {
        kotlin {
            ktlint("1.2.1")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events = setOf(
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_OUT
            )

            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }
}
