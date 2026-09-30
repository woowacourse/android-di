import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}