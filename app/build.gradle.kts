plugins {
    alias(libs.plugins.nordic.application.compose)
    alias(libs.plugins.nordic.hilt)
}

group = "no.nordicsemi.android.common"

android {
    namespace = "no.nordicsemi.android.common.test"
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":uilogger"))
    implementation(project(":uiscanner"))
    implementation(project(":navigation"))
    implementation(project(":permission"))

    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.hilt.navigation.compose)
}
