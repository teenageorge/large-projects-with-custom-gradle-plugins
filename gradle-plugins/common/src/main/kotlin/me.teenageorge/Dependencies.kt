package me.teenageorge

object Dependencies {
    val kotlinStdLib by lazy { "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}" }
    val springBootWebflux by lazy {"org.springframework.boot:spring-boot-starter-webflux:${Versions.springBoot}"}
    val springBootConfiguration by lazy { "org.springframework.boot:spring-boot-configuration-processor:${Versions.springBoot}" }
    val springBootStarterActuator by lazy { "org.springframework.boot:spring-boot-starter-actuator:${Versions.springBoot}" }
    val springBootActuator by lazy { "org.springframework.boot:spring-boot-actuator:${Versions.springBoot}" }
    val springBootActuatorAutoConfigure by lazy { "org.springframework.boot:spring-boot-actuator-autoconfigure:${Versions.springBoot}" }
    val jacksonKotlin by lazy { "com.fasterxml.jackson.module:jackson-module-kotlin" }
    val springBootStarterJdbc by lazy { "org.springframework.boot:spring-boot-starter-jdbc:${Versions.springBoot}" }
    val springBootStarterR2Dbc by lazy { "org.springframework.boot:spring-boot-starter-data-r2dbc:${Versions.springBoot}" }
    val springBootStarterValidator by lazy { "org.springframework.boot:spring-boot-starter-validation:${Versions.springBoot}" }
    val springBootStarterTest by lazy { "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}" }
    val springBootCloudStarterBootstrap by lazy { "org.springframework.cloud:spring-cloud-starter-bootstrap:${Versions.springBootCloudStarterBootstrap}" }
    val springMockk by lazy { "com.ninja-squad:springmockk:${Versions.springMockk}" }
    val springBootOpenApiWebfluxUi by lazy { "org.springdoc:springdoc-openapi-webflux-ui:${Versions.springBootOpenApiWebfluxUi}" }

    val testContainersPostgres by lazy { "org.testcontainers:postgresql:${Versions.testContainers}" }
    val testContainers by lazy { "org.testcontainers:testcontainers:${Versions.testContainers}" }

    val postgresql by lazy { "org.postgresql:postgresql:${Versions.postgres}" }
    val r2DbcPostgres by lazy { "io.r2dbc:r2dbc-postgresql" }
    val embeddedPostgresql by lazy {"com.playtika.testcontainers:embedded-postgresql:${Versions.embeddedPostgresql}" }
    val jooq by lazy { "org.jooq:jooq:${Versions.jooq}" }
    val jooqCodeGen by lazy {"org.jooq:jooq-codegen:${Versions.jooq}"}
    val jooqKotlin by lazy { "org.jooq:jooq-kotlin" }
    val jooqMetaExtensions by lazy { "org.jooq:jooq-meta-extensions" }

    val flywayCore by lazy { "org.flywaydb:flyway-core" }
    val jakartaXmlBindApi by lazy { "jakarta.xml.bind:jakarta.xml.bind-api:${Versions.jakartaXml}" }

    val micrometerPrometheus by lazy { "io.micrometer:micrometer-registry-prometheus" }

    val reactorKotlinExtensions by lazy { "io.projectreactor.kotlin:reactor-kotlin-extensions" }
    val reactorTest by lazy { "io.projectreactor:reactor-test" }
    val assertJCore by lazy { "org.assertj:assertj-core:${Versions.assertJCore}" }
    // logging
    val kotlinLogging by lazy { "io.github.microutils:kotlin-logging-jvm:${Versions.kotlinLogging}" }
    val junitJupiterTestContainers by lazy {"org.testcontainers:junit-jupiter:${Versions.testContainers}" }
    val junitJupiter by lazy { "org.junit.jupiter:junit-jupiter:${Versions.junitJupiter}" }

    val bcprovJdk15 by lazy { "org.bouncycastle:bcprov-jdk15on:${Versions.bcprovJdk15}" }
    val junitJupiterEngine by lazy {"org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiterEngine}" }
    val mockk by lazy { "io.mockk:mockk:${Versions.mockk}" }
    val konformJvm by lazy { "io.konform:konform-jvm:${Versions.konformJvm}" }
    val kotest by lazy { "io.kotest.extensions:kotest-assertions-konform-jvm:${Versions.kotest}" }
    val kotlinXHtmlJvm by lazy { "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinXHtmlJvm}" }
}