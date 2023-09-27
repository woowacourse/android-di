plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.8.21")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.2")
    testImplementation("org.assertj", "assertj-core", "3.22.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation(project(mapOf("path" to ":domain")))
    implementation("javax.inject:javax.inject:1")
}
