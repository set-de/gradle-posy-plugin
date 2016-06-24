package de.set.gradle.posy

import org.dm.gradle.plugins.bundle.BundleExtension
import org.dm.gradle.plugins.bundle.BundlePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
/**
 * Plugin for creating plugins for the POSY-OutputFactory.
 */
public class PosyPlugin implements Plugin<Project> {

    static final String SET_PLUGIN_REPOSITORY_URL = 'https://dl.bintray.com/set-de/maven/'

    static final String POSY_API_ARTIFACT = 'de.set.posy:posy-api:1.0.0'

    static final String BUNDLE_CONFIGURATION_NAME = 'bundle'
    static final String PLUGIN_EXTENSION_NAME = 'plugin'

    private Configuration bundleConfiguration
    private PosyPluginExtension extension

    @Override
    void apply(Project project) {

        project.plugins.apply(JavaPlugin)
        project.plugins.apply(BundlePlugin)

        configureRepository(project)
        configureDependencies(project)

        configureConfigurations(project)
        configureBundleExtension(project)
        configurePluginExtension(project)
    }

    def configureDependencies(Project project) {
        project.dependencies {
            compile POSY_API_ARTIFACT
        }
    }

    def configureRepository(Project project) {
        project.repositories {
            maven {
                url SET_PLUGIN_REPOSITORY_URL
            }
        }
    }

    void configurePluginExtension(Project project) {
        extension = project.extensions.findByName(PLUGIN_EXTENSION_NAME)
        if (!extension) {
            extension = project.extensions.create(PLUGIN_EXTENSION_NAME)
        }
    }

    void configureBundleExtension(Project project) {
        project.afterEvaluate {
            def bundleExtension = project.extensions.getByType(BundleExtension)
            bundleExtension.instructions << [
                    'Bundle-SymbolicName':project.name,
                    'Bundle-Version':project.version,
                    'Include-Resource': this.bundleConfiguration*.name.collect { "@${it}" }.join(','),
                    'Export-Service': this.extension.plugins.join(',')
            ]
        }
    }

    void configureConfigurations(Project project) {
        bundleConfiguration = project.configurations.findByName(BUNDLE_CONFIGURATION_NAME)
        if (!this.bundleConfiguration) {
            this.bundleConfiguration = project.configurations.create(BUNDLE_CONFIGURATION_NAME)
            def compileConfiguration = project.configurations.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME)
            compileConfiguration.extendsFrom(this.bundleConfiguration)
        }
    }
}
