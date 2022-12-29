rootProject.name = "consumer-service"

pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "./gradle/plugins")
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("../gradle-plugins")
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}