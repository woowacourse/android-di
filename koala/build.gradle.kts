plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "woowacourse.shopping.koala"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Reflection
    implementation(kotlin("reflect"))
    implementation("androidx.lifecycle:lifecycle-common:2.6.2")
    implementation("androidx.appcompat:appcompat:1.6.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
}
