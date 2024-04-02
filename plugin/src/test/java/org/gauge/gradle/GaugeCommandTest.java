package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GaugeCommandTest {

    private Project project;
    private GaugeExtension extension;

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply(GaugeConstants.GAUGE_PLUGIN_ID);
        extension = project.getExtensions().findByType(GaugeExtension.class);
        assertNotNull(extension, "extension not found");
    }

    @Test
    void testSpecsDirCommand() {
        assertEquals(List.of("specs"), new GaugeCommand(extension, project).getSpecsDir());
        extension.getSpecsDir().set("spec1 spec2");
        assertEquals(List.of("spec1", "spec2"), new GaugeCommand(extension, project).getSpecsDir());
        setProjectProperty(GaugeProperty.SPECS_DIR.getKey(), "project ");
        assertEquals(List.of("project"), new GaugeCommand(extension, project).getSpecsDir());
    }

    @Test
    void testEnvProperty() {
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "default"), new GaugeCommand(extension, project).getEnvironment());
        extension.getEnv().set("env");
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "env"), new GaugeCommand(extension, project).getEnvironment());
        setProjectProperty(GaugeProperty.ENV.getKey(), "project ");
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "project"), new GaugeCommand(extension, project).getEnvironment());
    }

    @Test
    void testTagsProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getTags());
        extension.getTags().set("tag");
        assertEquals(List.of(GaugeProperty.TAGS.getFlag(), "tag"), new GaugeCommand(extension, project).getTags());
        setProjectProperty(GaugeProperty.TAGS.getKey(), "tag1 & tag2");
        assertEquals(List.of(GaugeProperty.TAGS.getFlag(), "tag1 & tag2"), new GaugeCommand(extension, project).getTags());
    }

    private String getProjectPath(final String projectDir) {
        return Paths.get(projectDir).toAbsolutePath().toString();
    }

    @Test
    void testProjectDir() {
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), project.getProjectDir().getAbsolutePath()),
                new GaugeCommand(extension, project).getProjectDir());
        extension.getDir().set("/usr/ext");
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), getProjectPath("/usr/ext")),
                new GaugeCommand(extension, project).getProjectDir());
        extension.getDir().set("extDir");
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), Path.of(project.getProjectDir().getPath(), "extDir").toString()),
                new GaugeCommand(extension, project).getProjectDir());
        setProjectProperty(GaugeProperty.PROJECT_DIR.getKey(), "/project/dir");
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), getProjectPath("/project/dir")),
                new GaugeCommand(extension, project).getProjectDir());
        setProjectProperty(GaugeProperty.PROJECT_DIR.getKey(), "project/dir");
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), Path.of(project.getProjectDir().getPath(), "project", "dir").toString()),
                new GaugeCommand(extension, project).getProjectDir());
    }

    @Test
    void testFlagsWithAdditionalFlagsProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        extension.getAdditionalFlags().set("--flag1");
        assertEquals(List.of("--flag1"), new GaugeCommand(extension, project).getFlags());
        setProjectProperty(GaugeProperty.ADDITIONAL_FLAGS.getKey(), "--simple-console -v ");
        assertEquals(List.of("--simple-console", "-v"), new GaugeCommand(extension, project).getFlags());
    }

    @Test
    void testFlagWithInParallelAndNodesProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        extension.getInParallel().set(true);
        extension.getNodes().set(0);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag()), new GaugeCommand(extension, project).getFlags());
        extension.getNodes().set(2);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 2),
                new GaugeCommand(extension, project).getFlags());
        setProjectProperty(GaugeProperty.IN_PARALLEL.getKey(), false);
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        setProjectProperty(GaugeProperty.IN_PARALLEL.getKey(), "true");
        setProjectProperty(GaugeProperty.NODES.getKey(), 3);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 3),
                new GaugeCommand(extension, project).getFlags());
    }

    @Test
    void testRepeatAndFailedFlagsWithAdditionalFlagsProperty() {
        extension.getInParallel().set(true);
        extension.getNodes().set(2);
        final var flag = "--flag1";
        extension.getAdditionalFlags().set(flag);
        assertEquals(List.of(flag, GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 2),
                new GaugeCommand(extension, project).getFlags());
        // when --repeat or --failed flag is provided
        extension.getAdditionalFlags().set("--failed " + flag);
        // then it should exclude --parallel and --n flags
        assertEquals(List.of("--failed", flag), new GaugeCommand(extension, project).getFlags());
        setProjectProperty(GaugeProperty.ADDITIONAL_FLAGS.getKey(), "-v --repeat");
        assertEquals(List.of("-v", "--repeat"), new GaugeCommand(extension, project).getFlags());
    }

    @Test
    void testCanGetExecutablePath() {
        assertEquals("gauge", new GaugeCommand(extension, project).getExecutable());
        extension.getGaugeRoot().set("/opt/gauge");
        assertEquals(Path.of("/opt/gauge/bin/gauge").toString(),
                new GaugeCommand(extension, project).getExecutable());
    }
    
    private void setProjectProperty(final String key, final Object value) {
        project.getExtensions().getExtraProperties().set(key, value);
    }

}
