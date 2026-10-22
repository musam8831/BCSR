// :core:policy — notification timing rules (FR-21). Pure Kotlin, no Android dependency, so it is unit-testable on the JVM.
plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin { jvmToolchain(17) }

dependencies {
    implementation("com.batoulapps.adhan:adhan:1.2.1") // Salah times, Java edition of Adhan (MIT, RES-03); adhan2 needs Kotlin 2.4 / Java 21
    testImplementation("junit:junit:4.13.2")
}
