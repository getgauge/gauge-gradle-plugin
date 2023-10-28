package org.gauge.gradle;

import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

class Base {

    @TempDir
    File primaryProjectDir;
    File settingsFile;
    File buildFile;

    protected static final String GAUGE_TASK_PATH = ":gauge";

    @BeforeEach
    public void setup() {
        settingsFile = new File(primaryProjectDir, "settings.gradle");
        buildFile = new File(primaryProjectDir, "build.gradle");
    }

    protected void writeFile(File destination, String content) throws IOException {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(destination))) {
            output.write(content);
        }
    }

    protected void copyGaugeProjectToTemp(final String project) {
        copyGaugeProjectToTemp(project, primaryProjectDir);
    }

    protected void copyGaugeProjectToTemp(final String project, final File testProjectDir) {
        final Path gaugeProjectPath = Path.of("testProjects", project);
        try {
            final URL gaugeProject = Thread.currentThread().getContextClassLoader().getResource(gaugeProjectPath.toString());
            Assertions.assertNotNull(gaugeProject, "Could not find the gauge project");
            FileUtils.copyDirectory(new File(gaugeProject.toURI()), testProjectDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getApplyPluginsBlock() {
        return "plugins {id 'org.gauge'}\n"
                + "repositories {mavenLocal()\nmavenCentral()}\n"
                + "dependencies {testImplementation 'com.thoughtworks.gauge:gauge-java:+'}\n";
    }

    protected GradleRunner defaultGradleRunner() {
        return GradleRunner.create()
                .withProjectDir(primaryProjectDir)
                .withPluginClasspath();
    }

}
