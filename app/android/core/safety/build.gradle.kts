// :core:safety — helpline directory and release check (SAF-01, SAF-02, SR-01, SR-05).
plugins { id("org.jetbrains.kotlin.jvm") }
kotlin { jvmToolchain(17) }
dependencies { testImplementation("junit:junit:4.13.2") }
