plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = rootProject.providers.gradleProperty("diGroup").get()
version = rootProject.providers.gradleProperty("diVersion").get()

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

dependencies {
    implementation(libs.kotlin.reflect)

    testImplementation(libs.assertj.core)
    testImplementation(libs.junit)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = rootProject.providers.gradleProperty("diArtifact").get()

            pom {
                name = rootProject.name
                description = "A small runtime dependency injection library for Kotlin/JVM applications."
                url = "https://github.com/first-woosun/android-di"

                developers {
                    developer {
                        id = "first-woosun"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/first-woosun/android-di.git"
                    developerConnection = "scm:git:ssh://git@github.com/first-woosun/android-di.git"
                    url = "https://github.com/first-woosun/android-di"
                }
            }
        }
    }
}
