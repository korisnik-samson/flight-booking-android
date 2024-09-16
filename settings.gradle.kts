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
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // pivotal for chip navigation bar download
        // https://jitpack.io/#ismaeldivita/chip-navigation-bar/1.3.4
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Flight Manager"
include(":app")
 