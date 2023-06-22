plugins {
    alias(libs.plugins.nordic.library.compose)
    alias(libs.plugins.nordic.hilt)
    alias(libs.plugins.nordic.nexus)
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "permissions-nfc"
    POM_NAME = "Nordic library for checking required permissions."

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"

    POM_DEVELOPER_ID = "hiar"
    POM_DEVELOPER_NAME = "Himali Aryal"
    POM_DEVELOPER_EMAIL = "himali.aryal@nordicsemi.no"
}

android {
    namespace = "no.nordicsemi.android.common.permissions.nfc"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))
    implementation(project(":navigation"))
    
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
}