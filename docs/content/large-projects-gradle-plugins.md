title: Multi application builds and plugins in gradle
date: 29 Dec, 2022
tags: Gradle, Spring-boot, Kotlin, jOOQ, reactive, Postgres
author: Teena George
category: gradle
summary:
A non-trivial production ready server-side (a.k.a backend) application is built using multiple independently develop-able, deployable components. The components can be microservices, publishable libraries, documentation, adapters, test servers and so on. Most of the time, the build logic is repeated in these components.

This article explains a standard way to build these components  as a multi application build, with a focus on reusing repeatable build logic in the form of gradle plugins.

The solution is developed with production readiness in mind while limiting the features to a bare minimum. The solution uses Kotlin DSL for build scripts, but the same results can be achieved using Groovy DSL.

Unrelated to the scope of the article, the source code can be a reference for anyone developing microservices in Spring boot (reactive), Kotlin, Flyway, jOOQ and Postgres.

**Source code**: [large-projects-with-custom-gradle-plugins](https://github.com/teenageorge/large-projects-with-custom-gradle-plugins)

##Audience:
Developers who are familiar with gradle and Kotlin (main development language and build script). Familiarity with gradle 7 or above will make it easy to understand the article.
Multi application vs multi project build:

A multi project build has a root/parent project and multiple sub projects. This is a standard way of building a single software component. A frequently occurring example is, a microservice and a few reusable libraries. This build has a single settings.gradle(.kts) at the root. It has a build.gradle(.kts) at the root and at sub project level to define build tasks. Together these make a single software component.

A multi application build can have a number of composite builds (instead of hierarchical). Each component is an independent build, i.e. it has its own build.gradle(.kts) and settings.gradle(.kts) files. We can create custom builds by composing multiple components. This logic is defined in settings.gradle(.kts) file by “including” components.

##Gradle plugins:
Plugins are gradle’s way of grouping reusable build logic. The most basic plugin can reside directly within the build.gradle(.kts) but this cannot be reused between components. A second type of plugin is called precompiled script plugin - it is written in a .gradle(.kts) file directly under src/main/[java/kotlin/groovy]. These are publishable and sharable among components. A third type of plugin - the stand alone project - is the most versatile. This type of plugin can be developed either in Java, Kotlin or Groovy, i.e. you would be writing your build logic just as you would be writing your application logic.
We will focus on the second and third type of plugins in this article.

##Sample application
In this example, there are two microservices, independently deployable and have same tech stack - Spring boot, Kotlin, jOOQ, flyway, Postgres and gradle.
DB migrations are applied using flyway, db classes are generated using jOOQ.
Both the applications have a majority of common dependencies - mostly of the same version.

###Solution
Implement reusable build logic using gradle plugins. Publish them to an artifact repository like mavenCentral() or an internal corporate repository. Note that in the solution the plugins are not yet published.

Include one or more of these plugins in the applications to cumulatively define the type of the application, as well as the repeating build scripts.

###Action Items
####Abstract away into plugins:
1. **db-migration plugin**: Each service needs to handle database migrations. Apart from the migration script itself, the flyway migration and jOOQ code generation logic are repeatable. 
A basic precompiled script plugin is enough to handle this. The important bit to note is that the plugin is executed after the applied project is evaluated. 
`afterEvaluate` should be used with care as it can mess up your build execution. In the db-migration plugin (line #47 onwards), it is safe.

2. **dependencies plugin**: Dependencies and versions are another commonly repeated build logic. Instead of repeating the dependency declarations in each project, it is better to develop a plugin to handle dependency management. If a dependency needs to be added or version to be upgraded, it will be limited to one place. In the example, this plugin is implemented as a standalone project. Take a look at the plugin id creation part:

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
3. **custom application-type plugin**: The third plugin is created to define the type of the micro services - Spring boot + kotlin application defined as a custom type. This will also be a precompiled script plugin.
Note: The common plugin is used in the other two services, as a base plugin. This shows that plugins can be “layered”. For e.g. the db-migration’s plugin block:
```
plugins {
    id("me.teenageorge.my-dependencies")
    id("nu.studer.jooq")
    id("org.flywaydb.flyway")
}
```
The plugin block applies the custom `my-dependencies` plugin as well as two third party plugins which are necessary for flyway + jooq db migration strategies.

####Develop microservices:
1. **consumer-service** - it is a Spring boot (Reactive) application developed in Kotlin. It uses jOOQ for code generation (from DB schema) and flyway for schema migration. It uses a postgres database.
These microservices will use the above plugins, instead of applying individual plugins in each service. For e.g. consumer-service’s plugin block:
```
plugins {
    id("me.teenageorge.spring-boot-kotlin-app") version "1.0.0"
    id("me.teenageorge.migration") version "1.0.0"
}
```

2. **order-service** - also a Spring boot application developed in Kotlin. It uses jOOQ for code generation (from DB schema) and flyway for schema migration. It uses a postgres database.
####Build databases:
Two postgres databases for each of the microservices. Postgres:14-alpine image is used to start two DB containers.
####Plugging it all in:
The important bits are in the .gradle.kts files. The root large-projects-with-custom-gradle-plugins is like an umbrella project which is a monorepo containing the source code of different products. Take note that its build.gradle.kts is empty. The settings.gradle.kts “includes” the three builds - consumer-service, order-service and gradle-plugins. This is different from including a project in the multi-project hierarchical development, because this is a composite build. A composite build specifies which buildable components are to be grouped together.
Go over to the source code and take a look at how it is all implemented.
