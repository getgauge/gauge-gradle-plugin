package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PropertyManagerTest {


    @Mock
    private Project project;
    @Mock
    private ConfigurationContainer configContainer;

    private GaugeExtension extension;

    @Before
    public void setUp() {
        extension = new GaugeExtension();

        when(project.getConfigurations()).thenReturn(configContainer);
        when(project.getBuildDir()).thenReturn(new File("/blah"));
    }

    @Test
    public void classpathShouldBeEmptyIfNoTesRuntimeDependencies() {
        mockTestRuntimeClasspathConfiguration();
        PropertyManager manager = new PropertyManager(project, extension);

        manager.setProperties();

        assertThat(extension.getClasspath(), containsString(""));
    }

    @Test
    public void classpathShouldIncludeTestRuntimeClasspathConfigurations() {
        mockTestRuntimeClasspathConfiguration("blah.jar", "blah2.jar");
        PropertyManager manager = new PropertyManager(project, extension);

        manager.setProperties();

        assertThat(extension.getClasspath(), containsString("blah.jar:blah2.jar"));
    }

    private void mockTestRuntimeClasspathConfiguration(String... files) {
        Configuration config = mock(Configuration.class, RETURNS_DEEP_STUBS);
        when(config.getName()).thenReturn("");
        when(config.getAsFileTree().getFiles())
                .thenReturn(new LinkedHashSet<>(
                        Arrays.stream(files)
                                .map(File::new)
                                .collect(Collectors.toList())));
        when(configContainer.getByName("testRuntimeClasspath")).thenReturn(config);
    }

}