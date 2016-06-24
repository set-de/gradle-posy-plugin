package de.set.gradle.posy

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by nw on 24.06.2016.
 */
class PosyPluginTest extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()

    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
    }

    def "plugin adds build task"() {
        given:
        buildFile << """
            plugins {
                id 'de.set.gradle.posy-plugin'
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments('dependencies')
            .withPluginClasspath()
            .build()

        then:
        result.task('dependencies').outcome == TaskOutcome.SUCCESS
    }
}

