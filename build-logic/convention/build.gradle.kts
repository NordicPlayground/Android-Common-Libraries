/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "com.nordicsemi.android"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("application.compose") {
            id = "com.nordicsemi.android.application.compose"
            displayName = "Application with Compose"
            description = "Application plugin extension with Compose feature enabled"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("application") {
            id = "com.nordicsemi.android.application"
            displayName = "Standalone Application configuration"
            description = "Application plugin extension for internal releases"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("library.compose") {
            id = "com.nordicsemi.android.library.compose"
            displayName = "Library with Compose"
            description = "Library plugin extension with Compose feature enabled"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("library") {
            id = "com.nordicsemi.android.library"
            displayName = "Standalone library configuration"
            description = "Library plugin extension for internal releases"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("feature") {
            id = "com.nordicsemi.android.feature"
            displayName = "Feature plugin"
            description = "UI feature plugin with Hilt & Compose"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = "com.nordicsemi.android.hilt"
            displayName = "Hilt plugin"
            description = "Plugin enabling Hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}

publishing {
    repositories {
        gradlePluginPortal()
    }
}
