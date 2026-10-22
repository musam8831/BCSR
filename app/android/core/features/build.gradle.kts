// :core:features — FS-001 features (FR-08, FR-09). Pure Kotlin; must match ml/features/fs001.py.
plugins { id("org.jetbrains.kotlin.jvm") }
kotlin { jvmToolchain(17) }
dependencies { testImplementation("junit:junit:4.13.2") }
