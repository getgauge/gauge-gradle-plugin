/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.util.PropertyManager;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;

@SuppressWarnings("WeakerAccess")
public class ClasspathTask extends Test {

    @TaskAction
    public void classpath() {
        Project project = getProject();
        GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        PropertyManager propertyManager = new PropertyManager(project, extension);
        propertyManager.setProperties();
        System.out.println(extension.getClasspath());
    }
}