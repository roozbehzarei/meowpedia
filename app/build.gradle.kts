import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // kotlinx.serialization
    alias(libs.plugins.kotlinx.serialization)
    // Hilt
    alias(libs.plugins.hilt.android)
    // KSP
    alias(libs.plugins.ksp)
    // Kapt
    kotlin("kapt")
}

// Load properties from the key.properties file at the project root
val keyProperties =
    Properties().apply {
        val keyPropertiesFile = rootProject.file("key.properties")
        if (keyPropertiesFile.exists()) {
            load(FileInputStream(keyPropertiesFile))
        }
    }

// Retrieve The Cat API key from key.properties file
val catApiKey = keyProperties["CAT_API_KEY"] as String

android {
    namespace = "com.roozbehzarei.meowpedia"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.roozbehzarei.meowpedia"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CAT_API_KEY", "\"$catApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    // kotlinx.serialization
    implementation(libs.kotlinx.serialization.json)
    // Navigation
    implementation(libs.navigation.compose)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
    // Mockk
    testImplementation(libs.mockk)
    // Paging 3
    implementation(libs.androidx.paging.compose)
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
    // Custom Tab
    implementation(libs.androidx.browser)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
