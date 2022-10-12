plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.hilt")
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Fundamental components of compose UI
    implementation("androidx.compose.ui:ui")
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle.kts").exists()) {
    extra.set("POM_ARTIFACT_ID", "core")
    extra.set("POM_NAME", "Core Nordic common library.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle.kts"))
}