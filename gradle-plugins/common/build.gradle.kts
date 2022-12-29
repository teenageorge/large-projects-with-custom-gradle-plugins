plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "me.teenageorge"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("my-dependencies") {
            id = "me.teenageorge.my-dependencies"
            implementationClass = "me.teenageorge.plugin.DependencyPlugin"
        }
    }
}