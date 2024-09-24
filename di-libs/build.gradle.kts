plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    // junit5 & kotest
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("de.mannodermaus.junit5:android-test-runner:1.4.0")
}
