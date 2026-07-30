package org.gauge.gradle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

abstract class Base {

    public static final String SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM = "org.gauge.gradle.GradleTestVersion#supportedVersionsForCurrentJvm";

    @TempDir
    File testProjectDir;
    Path settingsFilePath;
    Path buildFilePath;

    protected static final String GAUGE_TASK_PATH = ":gauge";

    @BeforeEach
    void setup() {
        final Path testProjectPath = testProjectDir.toPath();
        settingsFilePath = testProjectPath.resolve("settings.gradle");
        buildFilePath = testProjectPath.resolve("build.gradle");
    }

    protected void writeFile(final Path destination, String content) throws IOException {
        Files.writeString(destination, content);
    }

    protected void copyGaugeFixtureToTemp()
        throws IOException {
        copyGaugeFixtureToTemp(testProjectDir, "simple-project");
    }

    protected void copyGaugeFixtureToTemp(final File testProjectDir, final String fixtureName) throws IOException {
        try {
            URL resource = Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader()
                    .getResource("fixtures/" + fixtureName),
                "Could not find fixture: " + fixtureName
            );
            FileUtils.copyDirectory(Path.of(resource.toURI()).toFile(), testProjectDir);
        } catch (IOException | URISyntaxException e) {
            throw new IOException("Failed to copy fixture: " + fixtureName, e);
        }
    }

    protected String getApplyPluginsBlock() {
        return """
            plugins {
              id 'org.gauge'
            }
            repositories {
              mavenCentral()
            }
            dependencies {
              testImplementation 'com.thoughtworks.gauge:gauge-java:+'
            }
            tasks.withType(AbstractTestTask).configureEach {
                if (GradleVersion.current().version >= '9') failOnNoDiscoveredTests = false
            }
            """;
    }

    protected GradleRunner defaultGradleRunner(GradleTestVersion gradle) {
        return GradleRunner.create()
            .withGradleVersion(gradle.version)
            .withProjectDir(testProjectDir)
            .withPluginClasspath();
    }

}
