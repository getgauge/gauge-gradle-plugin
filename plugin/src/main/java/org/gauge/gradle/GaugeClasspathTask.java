/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;

public abstract class GaugeClasspathTask extends DefaultTask {

    private final Project project;

    @Inject
    public GaugeClasspathTask(final Project project) {
        this.project = project;
        super.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        super.setDescription("Gets the classpath.");
        super.dependsOn("build");
    }

    @TaskAction
    public void classpath() {
        System.out.println(project.getExtensions().getByType(SourceSetContainer.class)
            .getByName("test")
            .getRuntimeClasspath()
            .getAsPath());
    }
}