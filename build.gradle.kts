/*
 * Copyright 2021 The Android Open Source Project
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
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.publish) apply false
    id("no.nordicsemi.android.feature") version "1.0.0" apply false
    id("no.nordicsemi.android.library") version "1.0.0" apply false
    id("no.nordicsemi.android.library.compose") version "1.0.0" apply false
    id("no.nordicsemi.android.application") version "1.0.0" apply false
    id("no.nordicsemi.android.application.compose") version "1.0.0" apply false
    id("no.nordicsemi.android.hilt") version "1.0.0" apply false
}

apply(plugin = "io.github.gradle-nexus.publish-plugin")
apply(from = rootProject.file("gradle/publish-root.gradle"))
