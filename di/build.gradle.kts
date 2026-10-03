plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = "com.github.nadajinny.android-di"
version = System.getenv("VERSION") ?: "0.0.0-local"

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "di"
        }
    }
}

dependencies {
    implementation(libs.kotlin.reflect)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
}
