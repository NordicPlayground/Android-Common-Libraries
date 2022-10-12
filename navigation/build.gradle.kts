plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.hilt")
    id("kotlin-parcelize")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Fundamental components of compose UI
    implementation("androidx.compose.ui:ui")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha03")

    // Dagger and Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle.kts").exists()) {
    extra.set("POM_ARTIFACT_ID", "navigation")
    extra.set("POM_NAME", "Nordic library for navigation framework.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle.kts"))
}
