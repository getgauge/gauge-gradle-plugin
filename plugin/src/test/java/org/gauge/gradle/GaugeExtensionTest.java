package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GaugeExtensionTest {

    private GaugeExtension extension;

    @BeforeEach
    void setUp() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(GaugeConstants.GAUGE_PLUGIN_ID);
        extension = project.getExtensions().findByType(GaugeExtension.class);
        assertNotNull(extension, "extension should not be null");
    }

    @Test
    public void testDefaultProperties() {
        assertEquals("default", extension.getEnv().get());
        assertFalse(extension.getTags().isPresent());
        assertEquals("specs", extension.getSpecsDir().get());
        assertFalse(extension.getInParallel().get());
        assertFalse(extension.getNodes().isPresent());
        assertTrue(extension.getEnvironmentVariables().get().isEmpty());
        assertFalse(extension.getAdditionalFlags().isPresent());
        assertFalse(extension.getGaugeRoot().isPresent());
    }

    @Test
    public void testExtensionProperties() {
        setExtensionProperties();
        assertEquals("test", extension.getEnv().get());
        assertEquals("(tag1|tag2)&tag3", extension.getTags().get());
        assertEquals("extension specs", extension.getSpecsDir().get());
        assertTrue(extension.getInParallel().get());
        assertEquals(4, extension.getNodes().get());
        assertEquals(Map.of("key", "value"), extension.getEnvironmentVariables().get());
        assertEquals("--verbose --flag", extension.getAdditionalFlags().get());
        assertEquals("/usr/local/", extension.getGaugeRoot().get());
    }

    private void setExtensionProperties() {
        extension.getEnv().set("test");
        extension.getTags().set("(tag1|tag2)&tag3");
        extension.getSpecsDir().set("extension specs");
        extension.getInParallel().set(true);
        extension.getNodes().set(4);
        extension.getEnvironmentVariables().set(Map.of("key", "value"));
        extension.getAdditionalFlags().set("--verbose --flag");
        extension.getGaugeRoot().set("/usr/local/");
    }
}
