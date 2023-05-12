/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import com.thoughtworks.gauge.gradle.util.ProcessBuilderFactory;
import com.thoughtworks.gauge.gradle.util.PropertyManager;
import com.thoughtworks.gauge.gradle.util.Util;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class GaugeTask extends Test {
    private final Logger log = LoggerFactory.getLogger("gauge");

    @TaskAction
    public void gauge() {
        Project project = getProject();
        GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        PropertyManager propertyManager = new PropertyManager(project, extension);
        propertyManager.setProperties();

        ProcessBuilderFactory processBuilderFactory = new ProcessBuilderFactory(extension, project);
        ProcessBuilder builder = processBuilderFactory.create();
        if (null != extension) {
            builder.environment().putAll(
                    extension.getEnvironmentVariables().entrySet().stream()
                            .filter(variable -> !builder.environment().containsKey(variable.getKey()))
                            .filter(variable -> Objects.nonNull(variable.getValue()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        log.info("Executing command => " + builder.command());

        try {
            Process process = builder.start();
            executeGaugeSpecs(process);
        } catch (IOException e) {
            if(e.getMessage().contains("Cannot run program \"gauge\": error=2, No such file or directory")){
                throw new GaugeExecutionFailedException("Gauge or Java runner is not installed! Refer https://docs.gauge.org/getting_started/installing-gauge.html");
            }
            log.error(e.getMessage() + e.getStackTrace());
        }
    }

    public void executeGaugeSpecs(Process process) throws GaugeExecutionFailedException {
        try {
            Util.inheritIO(process.getInputStream(), System.out);
            Util.inheritIO(process.getErrorStream(), System.err);
            if (process.waitFor() != 0) {
                throw new GaugeExecutionFailedException("Execution failed for one or more tests!");
            }
        } catch (InterruptedException | NullPointerException e) {
            throw new GaugeExecutionFailedException(e);
        }
    }
}