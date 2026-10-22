// :core:content — catalogue bundle verification and approved-only access (ADR-0005 §5–§6, FR-15, CR-02).
plugins { id("org.jetbrains.kotlin.jvm") }
kotlin { jvmToolchain(17) }
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0") // JsonElement API only; no compiler plugin
    implementation("org.bouncycastle:bcprov-jdk18on:1.86") // Ed25519: Android's own needs API 33, min SDK is 28
    testImplementation("junit:junit:4.13.2")
}
