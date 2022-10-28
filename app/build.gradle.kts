plugins {
    id("com.nordicsemi.android.application")
    id("com.nordicsemi.android.application.compose")
    id("com.nordicsemi.android.hilt")
}

group = "no.nordicsemi.android.common"

android {
    namespace = "no.nordicsemi.android.common.test"
    compileSdk = 33

    defaultConfig {
        applicationId = "no.nordicsemi.android.common.test"
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":uilogger"))
    implementation(project(":uiscanner"))
    implementation(project(":navigation"))
    implementation(project(":permission"))

    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")

    // Dagger and Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

}
