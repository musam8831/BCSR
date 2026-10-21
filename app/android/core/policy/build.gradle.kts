// :core:policy — notification timing rules (FR-21). Pure Kotlin, no Android dependency, so it is unit-testable on the JVM.
plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin { jvmToolchain(17) }

dependencies {
    testImplementation("junit:junit:4.13.2")
}
