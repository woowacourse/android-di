plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    implementation("javax.inject", "javax.inject", "1")
    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.8.21")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.2")
    testImplementation("org.assertj", "assertj-core", "3.22.0")

    // refelction
    implementation(kotlin("reflect"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}
