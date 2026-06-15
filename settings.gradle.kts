pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven { url = uri("https://maven.myket.ir") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.myket.ir") }
        google()
        mavenCentral()
    }
}

rootProject.name = "TODO List"
include(":app")

// Core modules
include(":core:common")
include(":core:network")
include(":core:local")
include(":core:domain")
include(":core:data")
include(":core:ui")

// Feature modules
include(":feature:todo-list")
include(":feature:todo-detail")
