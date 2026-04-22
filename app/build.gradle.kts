plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.20"
}

android {
    namespace = "net.obsidianx.watchchime"
    compileSdk = 37

    defaultConfig {
        applicationId = "net.obsidianx.watchchime"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.wear:wear:1.4.0")
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.wear.compose:compose-material3:1.7.0-alpha01")
    implementation("androidx.wear.compose:compose-foundation:1.7.0-alpha01")
    implementation("androidx.wear.compose:compose-ui-tooling:1.7.0-alpha01")
    implementation(platform("androidx.compose:compose-bom:2026.03.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
