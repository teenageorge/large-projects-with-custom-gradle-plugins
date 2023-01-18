title: <span style="color:#ff5f0e">Custom gradle plugins</span>
date: 29 Dec, 2022
tags: Gradle, Spring boot, Kotlin, jOOQ
category: gradle

#### Context of the problem:
Any non-trivial production-ready server-side application has multiple independently deployable components. Some examples are microservices, libraries, and documentation.

For many of these components, all or some of the build logic is similar, hence repeatable. This article explains how to reuse repeatable build logic by building custom Gradle plugins.

The solution is developed with production readiness in mind while limiting business features to a bare minimum. The solution uses Kotlin DSL for build scripts, but Groovy DSL is also a popular choice.

**<span style="color:#ff5f0e">Source code</span>**: [large-projects-with-custom-gradle-plugins](https://github.com/teenageorge/large-projects-with-custom-gradle-plugins)

## <span style="color:#ff5f0e">Audience:</span>
Developers familiar with Gradle and Kotlin (primary development language and build script). Familiarity with Gradle 7 or above will make it easy to understand the article.

## <span style="color:#ff5f0e">Gradle plugins:</span>
Plugins are Gradle's way of grouping reusable build logic.
The most basic plugin can reside directly within the `build.gradle.kts`, but sharing between components is impossible.

The second type of plugin is called precompiled script plugin. These are publishable and shareable among components. The plugin's implementation is in any of the `*.gradle.kts` file directly under src/main/kotlin.

The third type of plugin - the stand-alone project - is the most versatile. This type of plugin is a Kotlin project, i.e. you would be writing your build logic just as you would be writing your application logic.
This article will focus on the second and third types of plugins.

## <span style="color:#ff5f0e">Sample application:</span>
This example uses two microservices.  These are independently deployable and have the same tech stack - Spring boot, Kotlin, jOOQ, flyway, Postgres and Gradle.
DB migrations are applied using flyway, and jOOQ code-gen generates DB classes.
Both applications have a majority of common dependencies - mainly of the same version.
## <span style="color:#ff5f0e">Solution:</span>
Implement reusable build logic using Gradle plugins. Note that the solution makes use of internal plugins. It is better to publish the plugins to external repositories for production-ready applications. Then use them as binary plugins (without "including" them in the project path).

Include one or more of these plugins in the applications to cumulatively define the application type and the repeating build scripts.
### <span style="color:#ff5f0e">Action Items</span>
#### <span style="color:#ff5f0e">Abstract away into plugins:</span>
* **<span style="color:#ff5f0e">db-migration plugin</span>**: Each service needs to handle database migrations. Apart from the migration script itself, the flyway migration and jOOQ code generation logic are repeatable.
   A precompiled script plugin is enough to handle this. The flyway and jOOQ plugins' execution starts after the project evaluation phase.
   `afterEvaluate` should be used with care as it can potentially mess up your build execution.

* **<span style="color:#ff5f0e">dependencies plugin</span>**: Dependencies and versions are another commonly repeated build logic. Instead of repeating the dependency declarations in each project, we can develop a plugin to handle dependency management.
   The Kotlin project `common` defines a `DependencyPlugin` class. It extends `Plugin` and defines the plugin type as `Project`. The dependencies and versions are Kotlin `object`s - single static instances.
   The plugin is defined with a plugin-id in build'gradle.kts.

```
gradlePlugin {
    plugins {
        create("my-dependencies") {
            id = "me.teenageorge.my-dependencies"
            implementationClass = "me.teenageorge.plugin.DependencyPlugin"
        }
    }
}
```
* **<span style="color:#ff5f0e">custom application-type plugin</span>**: The third plugin defines the type of microservices - Spring boot + Kotlin application. This plugin will also be a precompiled script plugin.
   Note: The other two services use `common` as a base plugin. This approach shows that plugins can be “layered”. E.g. the db-migration’s plugin block:
```
plugins {
    id("me.teenageorge.my-dependencies")
    id("nu.studer.jooq")
    id("org.flywaydb.flyway")
}
```

The plugin block applies`my-dependencies` and two third-party plugins necessary for flyway + jOOQ DB migration strategies.

#### <span style="color:#ff5f0e">Create a couple of microservices:</span>
* **<span style="color:#ff5f0e">consumer-service</span>** - Creates and retrieves consumers. It is a Spring boot (Reactive) + Kotlin application with a PostgreSQL database. It uses jOOQ for code generation (from DB schema) and flyway for schema migration.
   These microservices will use the above plugins instead of individual plugins in each service. E.g. consumer service's plugin block:
```
plugins {
    id("me.teenageorge.spring-boot-kotlin-app") version "1.0.0"
    id("me.teenageorge.migration") version "1.0.0"
}
```

* **<span style="color:#ff5f0e">order-service</span>** - Creates and retrieves orders. For the scope of the solution, technical design is the same as consumer-service.
#### <span style="color:#ff5f0e">Databases:</span>
Two Postgres databases for each of the microservices. `Postgres:14-alpine` is used to start the DB containers.

#### <span style="color:#ff5f0e">Plugging it all in:</span>
The significant bits are in the `.gradle.kts` files. The root `large-projects-with-custom-gradle-plugins` is like an umbrella project which is a monorepo containing the source code of different products. Note that the `build.gradle.kts` at the root level is empty. The `settings.gradle.kts` is composed of three components - consumer-service, order-service and gradle-plugins.
Go over to the source code and see how it is all implemented.

#### <span style="color:#ff5f0e">References:</span>
1. [Developing custom gradle plugins](https://docs.gradle.org/current/userguide/custom_plugins.html)
2. [Structuring large software products](https://docs.gradle.org/current/userguide/structuring_software_products.html)