plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.20" apply false
    id("org.jetbrains.kotlin.android") version "2.2.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20" apply false
    id("com.android.application") version "8.13.1" apply false
    id("com.android.library") version "8.13.1" apply false
    id("org.cyclonedx.bom") version "2.2.0"
}

// CycloneDX SBOM (T-M5-015) needs a group and version on every project.
allprojects {
    group = "org.pide.bcsr"
    version = "0.1.0-dev"
}
