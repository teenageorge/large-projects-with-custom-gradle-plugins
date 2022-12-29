plugins {
    `kotlin-dsl`
}

group = "me.teenageorge"
version = "1.0.0"

ext {
    set("testcontainers.version", "1.17.5")
    set("springBootPlugin.version", "2.6.8")
    set("springDependencyPlugin.version", "1.0.11.RELEASE")
    set("kotlinPlugin.version", "1.6.21")
    set("jibPlugin.version", "3.2.1")
}

dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:${property("springBootPlugin.version")}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${property("kotlinPlugin.version")}")
    implementation("io.spring.gradle:dependency-management-plugin:${property("springDependencyPlugin.version")}")
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${property("kotlinPlugin.version")}")
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:${property("jibPlugin.version")}")
    implementation("org.testcontainers:postgresql:${property("testcontainers.version")}")
    implementation("org.testcontainers:testcontainers:${property("testcontainers.version")}")
}
