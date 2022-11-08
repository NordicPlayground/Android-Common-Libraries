plugins {
    id("no.nordicsemi.android.gradle.feature")
    id("no.nordicsemi.android.gradle.library.compose")
    id("no.nordicsemi.android.gradle.hilt")
    id("no.nordicsemi.android.gradle.nexus")
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "theme"
    POM_NAME = "Nordic library for Android with UI screens utilizing uiscanner library."
}

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
//if (rootProject.file("gradle/publish-module.gradle").exists()) {
//    extra.set("POM_ARTIFACT_ID", "theme")
//    extra.set("POM_NAME", "Nordic theme library for Android")
//    extra.set("POM_PACKAGING", "aar")
//    apply(from = rootProject.file("gradle/publish-module.gradle"))
//}

//apply(from = rootProject.file("gradle/aaa.gradle"))
//apply(plugin = "no.nordicsemi.android.gradle.nexus")

//nordicNexusPublishing {
//    POM_ARTIFACT_ID = "theme"
//    POM_NAME = "Nordic theme library for Android."
//}