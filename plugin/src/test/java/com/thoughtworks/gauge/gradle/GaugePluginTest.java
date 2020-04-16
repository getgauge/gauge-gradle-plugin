package com.thoughtworks.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.SortedMap;

import static org.junit.Assert.*;

public class GaugePluginTest {
    private static final String GAUGE = "gauge";
    private Project project;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void pluginShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE);
        assertTrue(project.getPlugins().getPlugin(GAUGE) instanceof GaugePlugin);
        assertFalse(project.getPlugins().getPlugin(GAUGE) instanceof JavaPlugin);
    }

    @Test
    public void taskShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE);
        TaskContainer tasks = project.getTasks();
        assertEquals(2, tasks.size());

        SortedMap<String, Task> tasksMap = tasks.getAsMap();
        Task gauge = tasksMap.get(GAUGE);
        Task classpath = tasksMap.get("classpath");

        assertTrue(gauge instanceof GaugeTask);
        assertTrue(classpath instanceof ClasspathTask);
    }
}