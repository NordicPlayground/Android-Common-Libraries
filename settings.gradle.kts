pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from(files("build-logic/toml/libs.versions.toml"))
        }
    }
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
