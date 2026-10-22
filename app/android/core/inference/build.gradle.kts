// :core:inference — StateEstimator and the cold-start rules model B1 (FR-10, FR-11, EP-001 §3).
plugins { id("org.jetbrains.kotlin.jvm") }
kotlin { jvmToolchain(17) }
dependencies {
    api(project(":core:features"))
    testImplementation("junit:junit:4.13.2")
}
