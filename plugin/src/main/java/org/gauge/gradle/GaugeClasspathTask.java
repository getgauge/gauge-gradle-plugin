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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task to print the test runtime classpath for Gauge.
 */
public abstract class GaugeClasspathTask extends DefaultTask {

    private static final Logger logger = LoggerFactory.getLogger("gauge");
    private final Project project;

    /**
     * Constructs a GaugeClasspathTask.
     *
     * @param project the Gradle project
     */
    @Inject
    protected GaugeClasspathTask(final Project project) {
        this.project = project;
        super.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        super.setDescription("Gets the classpath.");
        super.dependsOn("build");
    }

    /**
     * Prints the test runtime classpath to the logger.
     */
    @TaskAction
    public void classpath() {
        logger.info(project.getExtensions().getByType(SourceSetContainer.class)
            .getByName("test")
            .getRuntimeClasspath()
            .getAsPath());
    }
}