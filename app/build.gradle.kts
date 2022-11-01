plugins {
    id("no.nordicsemi.android.application")
    id("no.nordicsemi.android.application.compose")
    id("no.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

android {
    namespace = "no.nordicsemi.android.common.test"
    compileSdk = 33

    defaultConfig {
        applicationId = "no.nordicsemi.android.common.test"
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":uilogger"))
    implementation(project(":uiscanner"))
    implementation(project(":navigation"))
    implementation(project(":permission"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.activity.compose)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

}
