plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "olive.di"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    // Mockk
    testImplementation("io.mockk:mockk:1.13.5")
    // Robolectric
    testImplementation("org.robolectric:robolectric:4.13")
}
