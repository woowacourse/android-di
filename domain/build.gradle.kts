plugins {
    kotlin("jvm")
    id("maven-publish")
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
}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.hyemdooly.di"
            artifactId = "final"
            version = "1.0.0"
            from(components["kotlin"])
        }
    }
}
