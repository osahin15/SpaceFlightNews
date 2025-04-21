pluginManagement {
    repositories {
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
        google()
        mavenCentral()
    }
}

rootProject.name = "SpaceFlightNews"
include(":app")
include(":feature")

listOf(
    "data",
    "domain"
).forEach { name ->
    include(":core:$name")
    project(":core:$name").projectDir = file("core/$name")
}
include(":core:ui")
