plugins {
    id("no.nordicsemi.android.gradle.feature")
    id("no.nordicsemi.android.gradle.library")
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
    GROUP = "no.nordicsemi.android.common"

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
}

android {
    namespace = "no.nordicsemi.android.common.ui.scanner"
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
