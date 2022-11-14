plugins {
    id("no.nordicsemi.android.gradle.feature")
    id("no.nordicsemi.android.gradle.library")
    id("no.nordicsemi.android.gradle.library.compose")
    id("no.nordicsemi.android.gradle.hilt")
    id("no.nordicsemi.android.gradle.nexus")
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "uilogger"
    POM_NAME = "Nordic library for library for UI helpers utilizing nordic logger library."
    GROUP = "no.nordicsemi.android.common"

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
}

android {
    namespace = "no.nordicsemi.android.common.ui.logger"
}

dependencies {
    implementation(project(":theme"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.nordic.log)

    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
}
