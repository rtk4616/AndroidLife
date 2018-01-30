package com.camnter.gradle.plugin.reduce.dependency.packaging

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.camnter.gradle.plugin.reduce.dependency.packaging.transform.ReduceDependencyPackagingTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

/**
 * @author CaMnter
 */

class ReduceDependencyPackagingPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final ExtensionContainer extensions = project.extensions
        if (!project.plugins.hasPlugin(AppPlugin.class)) {
            println "[ReduceDependencyPackagingPlugin]   reduce-dependency-packaging-plugin requires the Android plugin to be configured"
            return
        }
        project.extensions.create('reduceDependencyPackagingExtension',
                ReduceDependencyPackagingExtension)
        final AppExtension android = extensions.getByType(AppExtension.class)
        android.registerTransform(
                new ReduceDependencyPackagingTransform(project, android.applicationVariants))
    }
}