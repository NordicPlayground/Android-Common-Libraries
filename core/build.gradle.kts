plugins {
    id("no.nordicsemi.android.feature")
    id("no.nordicsemi.android.library.compose")
    id("no.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(libs.androidx.compose.ui)
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "core")
    extra.set("POM_NAME", "Core Nordic common library.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}