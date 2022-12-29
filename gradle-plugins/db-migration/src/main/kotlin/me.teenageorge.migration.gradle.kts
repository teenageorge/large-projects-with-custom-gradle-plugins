import me.teenageorge.Dependencies
import me.teenageorge.Versions
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient
import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
    id("me.teenageorge.my-dependencies")
    id("nu.studer.jooq")
    id("org.flywaydb.flyway")
}

version = "1.0.0"

ext {
    set("embedded-postgresql.version", "2.2.0")
    set("postgresql.version", "42.3.6")
    set("testcontainers.version", "1.17.5")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
            jvmTarget = Versions.jvmTarget
        }
    }
}

val flywayMigration: Configuration = configurations.create("flywayMigration")

dependencies {
    flywayMigration(Dependencies.postgresql)
    jooqGenerator(Dependencies.postgresql)
    jooqGenerator(Dependencies.testContainersPostgres)
    // workaround of issue: https://github.com/etiennestuder/gradle-jooq-plugin/issues/209
    jooqGenerator(Dependencies.jakartaXmlBindApi)
    jooqGenerator(Dependencies.jooqMetaExtensions)
}

afterEvaluate {
    flyway {
        locations = arrayOf("filesystem:${projectDir}/src/main/resources/db/migration")
        url = project.extra["url"].toString()
        user = project.extra["username"].toString()
        password = project.extra["password"].toString()
        configurations = arrayOf("flywayMigration")
    }

    jooq {
        this.version.set(Versions.jooq)
        edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
        configurations.create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = project.extra["url"].toString()
                    user = project.extra["username"].toString()
                    password = project.extra["password"].toString()
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                        isPojosAsKotlinDataClasses = true
                    }
                    target.apply {
                        packageName = "${project.extra["packageName"]}"
                        directory = "${project.projectDir}/build/generated/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }

    tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
        // ensure database schema has been prepared by Flyway before generating the jOOQ sources
        dependsOn(tasks.named("flywayMigrate"))

        // declare Flyway migration scripts as inputs on the jOOQ task
        inputs.files(project.fileTree(project.extra["migrationDir"].toString()))
            .withPropertyName("migrations")
            .withPathSensitivity(PathSensitivity.RELATIVE)
        allInputsDeclared.set(true)
        doLast {
            cleanupContainers()
        }
    }
}

tasks.named<FlywayMigrateTask>("flywayMigrate") {
    doLast {
        val jooqTask = gradle.taskGraph.allTasks.firstOrNull {
            it.name.contains("generateJooq")
        }
        if(jooqTask == null) {
            cleanupContainers()
        }
    }
}

fun cleanupContainers() {
    val dockerConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build()
    //HTTP transport used by testcontainers
    val dockerHttpClient = ZerodepDockerHttpClient.Builder().dockerHost(dockerConfig.dockerHost).build()
    val dockerClient = DockerClientImpl.getInstance(dockerConfig, dockerHttpClient)
    dockerClient.listContainersCmd().exec().filter {
        "true" == it.labels["org.testcontainers"]
    }.forEach {
        dockerClient.stopContainerCmd(it.id).exec()
    }
}
