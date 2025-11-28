/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package org.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaLibraryPlugin;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // java plugin needs to be applied as Gauge relies on java test classpath
        project.getPluginManager().apply(JavaLibraryPlugin.class);

        final GaugeExtension extension = project.getExtensions().create(GaugeConstants.GAUGE_EXTENSION_ID, GaugeExtension.class);
        extension.getEnv().convention(project.getProviders().gradleProperty(GaugeProperty.ENV.getKey()).orElse("default"));
        extension.getSpecsDir().convention(project.getProviders().gradleProperty(GaugeProperty.SPECS_DIR.getKey()).orElse("specs"));
        extension.getInParallel().convention(project.getProviders().gradleProperty(GaugeProperty.IN_PARALLEL.getKey()).map(Boolean::parseBoolean).orElse(false));

        project.getTasks().create(GaugeConstants.GAUGE_TASK, GaugeTask.class);
        project.getTasks().create(GaugeConstants.GAUGE_VALIDATE_TASK, GaugeValidateTask.class);
        project.getTasks().create(GaugeConstants.GAUGE_CLASSPATH_TASK, GaugeClasspathTask.class);
    }
}