plugins {
    id("com.nordicsemi.android.feature")
    id("com.nordicsemi.android.library.compose")
    id("com.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(project(":theme"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.nordic.log)

    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "uilogger")
    extra.set("POM_NAME", "Nordic library for library for UI helpers utilizing nordic logger library.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}
