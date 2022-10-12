plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":navigation"))

    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle.kts").exists()) {
    extra.set("POM_ARTIFACT_ID", "permission")
    extra.set("POM_NAME", "Nordic library for checking required permissions.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle.kts"))
}
