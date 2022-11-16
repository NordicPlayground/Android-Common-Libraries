plugins {
    alias(libs.plugins.nordic.feature)
    alias(libs.plugins.nordic.library)
    alias(libs.plugins.nordic.library.compose)
    alias(libs.plugins.nordic.hilt)
    alias(libs.plugins.nordic.nexus)
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

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
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
