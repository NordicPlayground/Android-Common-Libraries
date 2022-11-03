pluginManagement {
//    includeBuild("build-logic")
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
//    versionCatalogs {
//        create("libs") {
//            from("no.nordicsemi.android:version-catalog:1.0.0")
//        }
//    }
}
rootProject.name = "Common Libraries"

include(":core")
include(":app")
include(":theme")
include(":uiscanner")
include(":uilogger")
include(":navigation")
include(":analytics")
include(":permission")
