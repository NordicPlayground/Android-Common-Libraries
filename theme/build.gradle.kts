plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.hilt")
}

group = "no.nordicsemi.android.common"

dependencies {
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    // Pager with indicators
    implementation("com.google.accompanist:accompanist-pager:0.27.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.27.0")
    // Markdown
    implementation("com.github.jeziellago:compose-markdown:0.3.0")
    implementation("androidx.activity:activity-compose:1.6.0")
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
