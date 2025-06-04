package org.gauge.gradle;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_PLUGIN_ID;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK_GROUP;
import static org.gauge.gradle.GaugeConstants.GAUGE_VALIDATE_TASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertInstanceOf(GaugePlugin.class, project.getPlugins().getPlugin(GAUGE_PLUGIN_ID));
        assertFalse(project.getPlugins().getPlugin(GAUGE_PLUGIN_ID) instanceof JavaPlugin);
    }

    @Test
    void gaugeTasksShouldDependOnBuild() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        project.getTasks().withType(AbstractGaugeTask.class).forEach(task -> assertTrue(
            task.getDependsOn().stream().anyMatch(dep -> "build".equals(dep.toString())),
            "Task " + task.getName() + " should depend on 'build'"
        ));
    }

    @Test
    public void taskShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        TaskContainer tasks = project.getTasks();
        var tasksMap = tasks.getAsMap();
        Task gauge = tasksMap.get(GAUGE_TASK);
        assertEquals(GAUGE_TASK_GROUP, gauge.getGroup());
        assertInstanceOf(GaugeTask.class, gauge);
        Task classpath = tasksMap.get(GAUGE_CLASSPATH_TASK);
        assertEquals(GAUGE_TASK_GROUP, classpath.getGroup());
        assertInstanceOf(GaugeClasspathTask.class, classpath);
        Task validate = tasksMap.get(GAUGE_VALIDATE_TASK);
        assertEquals(GAUGE_TASK_GROUP, validate.getGroup());
        assertInstanceOf(GaugeValidateTask.class, validate);
    }
}