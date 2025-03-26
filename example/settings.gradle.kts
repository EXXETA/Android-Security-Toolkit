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

rootProject.name = "Mobile Security Toolkit Example"
include(":app")

includeBuild("../") {
    dependencySubstitution {
        substitute(
            module("com.local.project:securitytoolkit"),
        ).using(project(":securitytoolkit"))
    }
}
