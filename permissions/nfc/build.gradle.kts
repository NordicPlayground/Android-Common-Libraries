plugins {
    alias(libs.plugins.nordic.library.compose)
    alias(libs.plugins.nordic.hilt)
}

android {
    namespace = "no.nordicsemi.android.common.permissions.nfc"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":navigation"))
    
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
}