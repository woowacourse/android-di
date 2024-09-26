plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    `maven-publish`
}

android {
    namespace = "com.kmlibs.supplin"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "com.kmlibs.supplin.fixtures.android.application.FakeApplication"
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
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("javax.inject:javax.inject:1")
    implementation("androidx.test:core-ktx:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.3")

    // Robolectric
    testImplementation("org.robolectric:robolectric:4.13")

    // mockk
    testImplementation("io.mockk:mockk:1.12.0")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.fragment:fragment-testing:1.8.3")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.kmkim2689"
                artifactId = "Supplin"
                version = "1.0.2"
                pom {
                    name.set("name")
                    description.set("description")
                }
            }
        }
    }
}
