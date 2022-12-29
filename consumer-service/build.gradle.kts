import me.teenageorge.Dependencies
import me.teenageorge.Versions

plugins {
    id("me.teenageorge.spring-boot-kotlin-app") version "1.0.0"
    id("me.teenageorge.migration") version "1.0.0"
}

dependencies {
    implementation(Dependencies.jooq)
    implementation(Dependencies.jooqCodeGen)
    runtimeOnly(Dependencies.r2DbcPostgres)
    runtimeOnly(Dependencies.postgresql)
    runtimeOnly(Dependencies.flywayCore)
    testImplementation(Dependencies.embeddedPostgresql)
}

version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

springBoot {
    mainClass.set("me.teenageorge.ConsumerServiceApplicationKt")
    buildInfo {
        properties {
            artifact = "consumer-service"
            version = System.getProperty("version") ?: "unspecified"
            group = "me.teenageorge"
            name = "consumer-service"
        }
    }
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

    withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        environment["spring.profiles.active"] = "default"
        environment["spring.output.ansi.console-available"] = true
    }
}

configure<ProcessResources>("processResources") {
    filesMatching("**/*.yml") {
        expand(project.properties)
    }
}

inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (this.tasks.getByName(name) as C).configuration()
}

configurations.create("db-config") {
    project.extra["dbname"] = "consumer"
    project.extra["username"] = "user"
    project.extra["password"] = "password"
    project.extra["packageName"] = "me.teenageorge"
    project.extra["migrationDir"] = "${projectDir}/src/main/kotlin/resources/db/migration/"

    val dbInstance = org.testcontainers.containers.PostgreSQLContainer<Nothing>(Versions.postgresTestContainersImageName)

    dbInstance.apply {
        withDatabaseName(project.extra["dbname"].toString())
        withUsername(project.extra["username"].toString())
        withPassword(project.extra["password"].toString())
    }
    dbInstance.start()
    project.extra["url"] = dbInstance.jdbcUrl
    project.extra["containerId"] = dbInstance.containerId
}

