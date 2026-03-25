plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
    group = property("group") as String
    version = property("version") as String

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
