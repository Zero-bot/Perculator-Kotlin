plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
    `maven-publish`
    signing
}

kotlin {
    explicitApi()
    jvmToolchain(21)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}")
    }
}

dependencies {
    api(project(":perculator-core"))
    compileOnly(libs.spring.boot.autoconfigure)
    compileOnly(libs.spring.boot.starter.web)
    annotationProcessor(libs.spring.boot.configuration.processor)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("Perculator Spring Boot Starter")
                description.set("Spring Boot auto-configuration for Perculator WAF")
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
