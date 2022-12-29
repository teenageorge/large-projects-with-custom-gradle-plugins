package me.teenageorge.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class DependencyPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        /**
         * This plugin doesn't do anything other than exporting dependencies and versions as a plugin.
         */
    }
}