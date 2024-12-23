plugins {
    alias(libs.plugins.nordic.feature)
    alias(libs.plugins.nordic.nexus.android)
}
group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "permissions-wifi"
    POM_NAME = "Nordic library for checking Wi-Fi state and permissions required for scanning for Wi-Fi networks."

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
}

android {
    namespace = "no.nordicsemi.android.common.permissions.wifi"
}

dokka {
    dokkaSourceSets.named("main") {
        includes.from("Module.md")
    }
}

dependencies {
    implementation(project(":ui"))

    implementation(libs.androidx.compose.material.iconsExtended)
}