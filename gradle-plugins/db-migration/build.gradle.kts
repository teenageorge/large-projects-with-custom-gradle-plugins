plugins {
    `kotlin-dsl`
}

group = "me.teenageorge"
version = "1.0.0"

ext {
    set("postgresql.version", "42.3.6")
    set("testcontainers.version", "1.17.5")
    set("flyway-plugin.version", "8.5.13")
    set("jooq.version", "3.16.6")
    set("kotlinPlugin.version", "1.6.21")
    set("jooqPlugin.version", "7.1.1")
    set("docker-java.version", "3.2.13")
    set("jackson-module-kotlin.version", "2.14.0-rc2")
    set("embedded-postgresql.version", "2.2.0")
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.testcontainers:postgresql:1.17.5")
    }
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${property("kotlinPlugin.version")}")
    implementation("gradle.plugin.org.flywaydb:gradle-plugin-publishing:${property("flyway-plugin.version")}")
    implementation("nu.studer:gradle-jooq-plugin:${property("jooqPlugin.version")}")
    implementation("com.github.docker-java:docker-java:${property("docker-java.version")}")
    implementation("org.jooq:jooq:${property("jooq.version")}")
    implementation("org.jooq:jooq-kotlin:${property("jooq.version")}")
    implementation("org.flywaydb:flyway-core:${property("flyway-plugin.version")}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${property("jackson-module-kotlin.version")}")
    implementation("org.testcontainers:postgresql:${property("testcontainers.version")}")
    runtimeOnly("org.postgresql:postgresql:${property("postgresql.version")}")

    // test/containers
    implementation("com.playtika.testcontainers:embedded-postgresql:${property("embedded-postgresql.version")}")
    implementation("org.testcontainers:junit-jupiter:${property("testcontainers.version")}")

    implementation("org.jooq:jooq-meta-extensions:${property("jooq.version")}")
}