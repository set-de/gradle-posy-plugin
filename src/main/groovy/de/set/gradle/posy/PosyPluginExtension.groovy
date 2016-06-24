package de.set.gradle.posy

import org.gradle.api.Project

/**
 * Extension for configuring a POSY Plugin.
 */
class PosyPluginExtension {

    protected Set<String> plugins = []

    private Project project

    PosyPluginExtension() {
    }

    void service(String... serviceClass) {
        this.plugins.addAll(serviceClass)
    }
}
