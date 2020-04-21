package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PropertyManagerTest {


    @Mock
    private Project project;

    @Mock
    private SourceSetContainer sourceSetContainer;

    private GaugeExtension extension;

    @Before
    public void setUp() {
        extension = new GaugeExtension();
        Map<String,Object> properties = new HashMap<String, Object>();
        properties.put("sourceSets",sourceSetContainer);
        doReturn(properties).when(project).getProperties();
    }

    @Test
    public void classpathShouldBeEmptyIfNoTesRuntimeDependencies() {
        SourceSet config = mock(SourceSet.class, RETURNS_DEEP_STUBS);
        when(config.getName()).thenReturn("");
        when(config.getRuntimeClasspath().getAsPath())
                .thenReturn("");
        when(sourceSetContainer.getByName("test")).thenReturn(config);
         PropertyManager manager = new PropertyManager(project, extension);

         manager.setProperties();

         assertThat(extension.getClasspath(), containsString(""));
    }

    @Test
    public void classpathShouldIncludeTestRuntimeClasspathConfigurations() {
        SourceSet config = mock(SourceSet.class, RETURNS_DEEP_STUBS);
        when(config.getName()).thenReturn("");
        when(config.getRuntimeClasspath().getAsPath())
                .thenReturn(String.join(File.pathSeparator, "blah.jar", "blah2.jar"));
        when(sourceSetContainer.getByName("test")).thenReturn(config);
         PropertyManager manager = new PropertyManager(project, extension);

         manager.setProperties();

         assertThat(extension.getClasspath(), containsString(String.join(File.pathSeparator, "blah.jar", "blah2.jar")));
    }



}