plugins {
    id("com.nordicsemi.android.feature")
    id("com.nordicsemi.android.library.compose")
    id("com.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pagerindicators)

    implementation(libs.markdown)
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "theme")
    extra.set("POM_NAME", "Nordic theme library for Android")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}
