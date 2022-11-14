plugins {
    id("no.nordicsemi.android.gradle.feature")
    id("no.nordicsemi.android.gradle.library.compose")
    id("no.nordicsemi.android.gradle.hilt")
    id("no.nordicsemi.android.gradle.nexus")
}

group = "no.nordicsemi.android.common"

nordicNexusPublishing {
    POM_ARTIFACT_ID = "core"
    POM_NAME = "Core Nordic common library."
    GROUP = "no.nordicsemi.android.common"
}

android {
    namespace = "no.nordicsemi.android.common.core"
}

dependencies {
    implementation(libs.androidx.compose.ui)
}
