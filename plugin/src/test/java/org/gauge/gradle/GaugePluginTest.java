package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_PLUGIN_ID;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK_GROUP;
import static org.gauge.gradle.GaugeConstants.GAUGE_VALIDATE_TASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GaugePluginTest {
    private Project project;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void pluginShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        assertTrue(project.getPluginManager().hasPlugin("java"));
        assertTrue(project.getPluginManager().hasPlugin(GAUGE_PLUGIN_ID));
        assertTrue(project.getPlugins().getPlugin(GAUGE_PLUGIN_ID) instanceof GaugePlugin);
        assertFalse(project.getPlugins().getPlugin(GAUGE_PLUGIN_ID) instanceof JavaPlugin);
    }

    @Test
    public void taskShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        TaskContainer tasks = project.getTasks();
        var tasksMap = tasks.getAsMap();
        Task gauge = tasksMap.get(GAUGE_TASK);
        assertEquals(GAUGE_TASK_GROUP, gauge.getGroup());
        assertTrue(gauge instanceof GaugeTask);
        Task classpath = tasksMap.get(GAUGE_CLASSPATH_TASK);
        assertEquals(GAUGE_TASK_GROUP, classpath.getGroup());
        assertTrue(classpath instanceof GaugeClasspathTask);
        Task validate = tasksMap.get(GAUGE_VALIDATE_TASK);
        assertEquals(GAUGE_TASK_GROUP, validate.getGroup());
        assertTrue(validate instanceof GaugeValidateTask);
    }
}