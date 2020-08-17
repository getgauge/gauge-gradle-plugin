/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

 package com.thoughtworks.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GaugePlugin implements Plugin<Project> {

    private static final String GAUGE = "gauge";
    private static final String CLASSPATH = "classpath";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(GAUGE, GaugeExtension.class);
        project.getTasks().create(GAUGE, GaugeTask.class);
        project.getTasks().create(CLASSPATH, ClasspathTask.class);
    }
}