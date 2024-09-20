plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Inject
    implementation("javax.inject:javax.inject:1")

    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")

    // MockK
    testImplementation("io.mockk:mockk:1.13.8")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}
