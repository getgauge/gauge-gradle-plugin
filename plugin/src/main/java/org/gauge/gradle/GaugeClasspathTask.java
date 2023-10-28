/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package org.gauge.gradle;

import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;

public abstract class GaugeClasspathTask extends Test {

    public GaugeClasspathTask() {
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.setDescription("Gets the classpath.");
    }

    @TaskAction
    public void classpath() {
        System.out.println(getClasspath().getAsPath());
    }
}