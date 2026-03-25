plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    signing
}

kotlin {
    explicitApi()
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.jakarta.servlet.api)
    api(libs.jackson.module.kotlin)
    api(libs.jackson.databind)
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.assertj.core)
    testImplementation(libs.jakarta.servlet.api)
    testRuntimeOnly(libs.logback.classic)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("Perculator Core")
                description.set("Application-level Web Application Firewall engine for JVM")
                url.set("https://github.com/Zero-bot/Perculator-Kotlin")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Zero-bot/Perculator-Kotlin.git")
                    url.set("https://github.com/Zero-bot/Perculator-Kotlin")
                }
            }
        }
    }
}

signing {
    isRequired = findProperty("signing.keyId") != null
    sign(publishing.publications["mavenJava"])
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.named("javadoc"))
}
