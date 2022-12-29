dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "gradle-plugins"
include("common")
include("spring-boot-kotlin-app")
include("db-migration")