plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.tinderlikeapp.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
}

dependencies {
    implementation(project(":data"))
    implementation(project(":test"))
    implementation(project(":utils"))

    implementation(libs.gson)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Test
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.cash.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
}