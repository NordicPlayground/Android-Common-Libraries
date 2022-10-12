plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(project(":theme"))

    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // nRF Logger
    implementation("no.nordicsemi.android:log:2.3.0")

    // Dagger and Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle.kts").exists()) {
    extra.set("POM_ARTIFACT_ID", "uilogger")
    extra.set("POM_NAME", "Nordic library for library for UI helpers utilizing nordic logger library.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle.kts"))
}
