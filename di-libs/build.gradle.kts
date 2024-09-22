plugins {
    id("org.jetbrains.kotlin.android") // TODO: di-andorid-libs 모듈 분리해야할듯
//    id("org.jetbrains.kotlin.jvm")
    id("com.android.library") // TODO: di-andorid-libs 모듈 분리해야할듯
}

android {
    namespace = "woowa.shopping.di.libs"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // android //TODO : di-andorid-libs 모듈 분리해야할듯
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.activity:activity-ktx:1.9.1")
    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    // junit5 & kotest
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("de.mannodermaus.junit5:android-test-runner:1.4.0")
}
