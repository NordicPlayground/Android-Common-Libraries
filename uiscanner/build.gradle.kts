plugins {
    id("com.nordicsemi.android.feature")
    id("com.nordicsemi.android.library.compose")
    id("com.nordicsemi.android.hilt")
    id("kotlin-parcelize")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":permission"))

    implementation("com.google.accompanist:accompanist-flowlayout:0.27.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    // Material design icons
    implementation("androidx.compose.material:material-icons-extended")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Brings the new BluetoothLeScanner API to older platforms
    implementation("no.nordicsemi.android.support.v18:scanner:1.6.0")

    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "uiscanner")
    extra.set("POM_NAME", "Nordic library for Android with UI screens utilizing uiscanner library.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}
