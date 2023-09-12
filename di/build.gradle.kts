plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.8.21")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.glo.di"
            artifactId = "glo-di"
            version = "1.0"

            from(components["kotlin"])
        }
    }
}
