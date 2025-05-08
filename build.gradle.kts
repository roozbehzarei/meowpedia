// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // kotlinx.serialization
    alias(libs.plugins.kotlinx.serialization) apply false
    // Hilt
    alias(libs.plugins.hilt.android) apply false
    // KSP
    alias(libs.plugins.ksp) apply false
    // Kapt
    kotlin("kapt") version "2.1.20" apply false
}