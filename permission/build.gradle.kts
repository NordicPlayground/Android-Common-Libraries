plugins {
    id("no.nordicsemi.android.feature")
    id("no.nordicsemi.android.library.compose")
    id("no.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":navigation"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "permission")
    extra.set("POM_NAME", "Nordic library for checking required permissions.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}
