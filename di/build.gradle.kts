plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.woowa.di"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("junit:junit:4.13.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation("javax.inject:javax.inject:1")
    implementation("org.robolectric:robolectric:4.13")


    // test
    implementation("androidx.lifecycle:lifecycle-runtime-testing:2.8.5")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("androidx.arch.core:core-testing:2.2.0")
    implementation("com.google.truth:truth:1.1.3")
    implementation("androidx.arch.core:core-testing:2.2.0")
    implementation("androidx.test.ext:junit:1.2.1")
}
