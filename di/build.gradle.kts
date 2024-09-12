plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
}
