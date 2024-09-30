plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "woowacourse.shopping.hilt"
    compileSdk = 34

    defaultConfig {
        applicationId = "woowacourse.shopping.hilt"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
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
    dataBinding {
        enable = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
}

dependencies {
    // hilt
    val hilt = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")
    testImplementation("com.google.dagger:hilt-android-testing:$hilt")
    testImplementation("com.google.dagger:hilt-android-compiler:$hilt")
    // android
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("javax.inject:javax.inject:1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // Robolectric
    testImplementation("org.robolectric:robolectric:4.13")
    // junit5 & kotest
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.8.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("de.mannodermaus.junit5:android-test-runner:1.4.0")
}

kapt {
    correctErrorTypes = true
}