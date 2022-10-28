plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
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

    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    // Material 3
    implementation("androidx.compose.material3:material3")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Dagger and Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Firebase for Analytics
    implementation(platform("com.google.firebase:firebase-bom:31.0.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

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
