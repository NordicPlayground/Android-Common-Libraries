plugins {
    alias(libs.plugins.nordic.library.compose)
    alias(libs.plugins.nordic.hilt)
    alias(libs.plugins.nordic.nexus)
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "analytics"
    POM_NAME = "Nordic library for analytics."
    GROUP = "no.nordicsemi.android.common"

    POM_DESCRIPTION = "Nordic Android Common Libraries"
    POM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_URL = "https://github.com/NordicPlayground/Android-Common-Libraries"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/Android-Common-Libraries.git"
}

android {
    namespace = "no.nordicsemi.android.common.analytics"

     buildTypes {
         release {
             isMinifyEnabled = false
         }
     }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))

    implementation(libs.androidx.dataStore.preferences)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
