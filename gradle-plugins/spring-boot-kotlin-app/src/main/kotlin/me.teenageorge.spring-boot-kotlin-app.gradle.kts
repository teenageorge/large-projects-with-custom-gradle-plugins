import me.teenageorge.Dependencies
import me.teenageorge.Versions

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.google.cloud.tools.jib")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.spring.dependency-management")
    id("me.teenageorge.my-dependencies")
}

version = "1.0.0"

dependencies {
    // kotlin
    implementation(Dependencies.kotlinStdLib)
    // reactor
    implementation(Dependencies.reactorKotlinExtensions)
    // logging
    implementation(Dependencies.kotlinLogging)
    //spring
    implementation(Dependencies.springBootWebflux)
    implementation(Dependencies.springBootStarterActuator)
    annotationProcessor(Dependencies.springBootConfiguration)
    //persistence
    implementation(Dependencies.springBootStarterJdbc)
    implementation(Dependencies.springBootStarterR2Dbc)
    // management
    implementation(Dependencies.springBootActuator)
    implementation(Dependencies.springBootActuatorAutoConfigure)
    //validator
    implementation(Dependencies.springBootStarterValidator)
    // test
    testImplementation(Dependencies.springBootStarterTest)
    testImplementation(Dependencies.springBootCloudStarterBootstrap)
    // test/reactor
    testImplementation(Dependencies.reactorTest)
    testImplementation(Dependencies.springMockk)
    //documentation
    implementation(Dependencies.springBootOpenApiWebfluxUi)
    // metrics
    implementation(Dependencies.micrometerPrometheus)

    implementation(Dependencies.junitJupiter)
    // serialization
    implementation(Dependencies.jacksonKotlin)
    implementation(Dependencies.testContainersPostgres)
    testImplementation(Dependencies.bcprovJdk15)
    // test/engine
    testRuntimeOnly(Dependencies.junitJupiterEngine)
    // test/mock
    testImplementation(Dependencies.mockk)
    implementation(Dependencies.konformJvm)
    // assertJ
    testImplementation(Dependencies.assertJCore)
    //Kotest
    testImplementation(Dependencies.kotest)
    // source code analysis
    runtimeOnly(Dependencies.kotlinXHtmlJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {

    withType<Test> {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
            jvmTarget = Versions.jvmTarget
        }
    }

}