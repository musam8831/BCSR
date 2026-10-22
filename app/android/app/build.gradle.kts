// :app — the BCSR Android app (ARC-02 §7: features live in packages of this module until a split is needed).
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "org.pide.bcsr"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.pide.bcsr"
        minSdk = 28 // ADR-0001
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0-dev"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Release signing comes from the Play internal track setup (T-M5-016); no keys in the repo.
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging { resources.excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF" }
}

kotlin { jvmToolchain(17) }

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:features"))
    implementation(project(":core:inference"))
    implementation(project(":core:recommender"))
    implementation(project(":core:content"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0") // reads catalogue item JSON in the UI
    implementation(project(":core:policy"))
    implementation(project(":core:safety"))
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    testImplementation("junit:junit:4.13.2")
}
