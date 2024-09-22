plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.mylibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // android
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(project(":app"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.8.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("de.mannodermaus.junit5:android-test-runner:1.4.0")

    // Robolectric
    testImplementation("org.robolectric:robolectric:4.13")
}
