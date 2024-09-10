plugins {
    alias(libs.plugins.nordic.feature)
    alias(libs.plugins.nordic.nexus.android)
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "permissions-notification"
    POM_NAME = "Nordic library for checking Notification permission."

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
}

android {
    namespace = "no.nordicsemi.android.common.permissions.notification"
}

dependencies {
    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.compose.material.iconsExtended)
}