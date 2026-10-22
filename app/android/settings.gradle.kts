// BCSR product build (module layout: ARC-02 §2, §7). The spike app in spikes/ is a separate build.
pluginManagement {
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
    }
}
rootProject.name = "bcsr"
include(":app")
include(":core:policy", ":core:features", ":core:inference", ":core:recommender", ":core:content", ":core:safety")
include(":core:data")
