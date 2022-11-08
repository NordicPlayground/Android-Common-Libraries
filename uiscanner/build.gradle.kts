plugins {
    id("no.nordicsemi.android.gradle.feature")
    id("no.nordicsemi.android.gradle.library.compose")
    id("no.nordicsemi.android.gradle.hilt")
    id("no.nordicsemi.android.gradle.nexus")
    id("kotlin-parcelize")
    id("maven-publish")
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "uiscanner"
    POM_NAME = "Nordic library for Android with UI screens utilizing uiscanner library."
}

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":permission"))

    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.swiperefresh)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.activity.compose)
    implementation(libs.nordic.scanner)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
}

//artifacts {
//    archives androidSourcesJar
//}
//
//
//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//
//        }
//        release(MavenPublication) {
//
//        }
//        release(MavenPublication) {
//            artifac
//        }
//        artifacts {
//            sourceJar
//        }
//    }
//}

//// === Maven Central configuration ===
//// The following file exists only when Android BLE Library project is opened, but not
//// when the module is loaded to a different project.
//if (rootProject.file("gradle/publish-module.gradle").exists()) {
//    extra.set("POM_ARTIFACT_ID", "uiscanner")
//    extra.set("POM_NAME", "Nordic library for Android with UI screens utilizing uiscanner library.")
//    extra.set("POM_PACKAGING", "aar")
//    apply(from = rootProject.file("gradle/publish-module.gradle"))
//}
