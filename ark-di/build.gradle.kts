plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(project(":domain"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    implementation(kotlin("reflect"))
    // Mockk
    testImplementation("io.mockk:mockk:1.12.2")
    // AssertJ
    testImplementation("org.assertj:assertj-core:3.24.2")
}
