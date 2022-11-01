plugins {
    id("no.nordicsemi.android.feature")
    id("no.nordicsemi.android.library.compose")
    id("com.google.protobuf")
}

group = "no.nordicsemi.android.common"

//protobuf {
//
//    protoc {
//        artifact = "com.google.protobuf:protoc:3.14.0"
//    }
//
//    // Generates the java Protobuf-lite code for the Protobufs in this project. See
//    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
//    // for more information.
//    generateProtoTasks {
//        all().each { task ->
//            task.builtins {
//                java {
//                    option = "lite"
//                }
//            }
//        }
//    }
//}

dependencies {
    implementation(project(":core"))
    implementation(project(":theme"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

}

// === Maven Central configuration ===
// The following file exists only when Android BLE Library project is opened, but not
// when the module is loaded to a different project.
if (rootProject.file("gradle/publish-module.gradle").exists()) {
    extra.set("POM_ARTIFACT_ID", "analytics")
    extra.set("POM_NAME", "Nordic library for analytics.")
    extra.set("POM_PACKAGING", "aar")
    apply(from = rootProject.file("gradle/publish-module.gradle"))
}
