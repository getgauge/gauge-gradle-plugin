/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import java.io.File;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class PropertyManager {
    private static final String ENV = "env";
    private static final String TAGS = "tags";
    private static final String NODES = "nodes";
    private static final String SPECS_DIR = "specsDir";
    private static final String IN_PARALLEL = "inParallel";
    private static final String ADDITIONAL_FLAGS = "additionalFlags";
    private static final String GAUGE_ROOT = "gaugeRoot";
    private static final String FAILED = "--failed";
    private static final String REPEAT = "--repeat";

    private Project project;
    private GaugeExtension extension;
    private Map<String, ?> properties;

    public PropertyManager(Project project, GaugeExtension extension) {
        this.project = project;
        this.extension = extension;
        this.properties = project.getProperties();
    }

    public void setProperties() {
        setTags();
        setInParallel();
        setNodes();
        setEnv();
        setAdditionalFlags();
        setClasspath();
        setSpecsDir();
        setGaugeRoot();
    }

    private void setSpecsDir() {
        String specsDir = (String) properties.get(SPECS_DIR);
        if (specsDir != null) {
            extension.setSpecsDir(specsDir);
        }
    }

    private void setInParallel() {
        String inParallel = (String) properties.get(IN_PARALLEL);
        if (inParallel != null) {
            extension.setInParallel("true".equals(inParallel));
        }
    }

    private void setNodes() {
        String nodes = (String) properties.get(NODES);
        if (nodes != null) {
            extension.setNodes(Integer.parseInt(nodes));
        }
    }

    private void setTags() {
        String tags = (String) properties.get(TAGS);
        if (tags != null) {
            extension.setTags(tags);
        }
    }

    private void setEnv() {
        String env = (String) properties.get(ENV);
        if (env != null) {
            extension.setEnv(env);
        }
    }

    private void setAdditionalFlags() {
        String flags = (String) properties.get(ADDITIONAL_FLAGS);
        if (flags != null) {
            extension.setAdditionalFlags(flags);
            if (flags.contains(FAILED) || flags.contains(REPEAT)) {
                extension.setSpecsDir(null);
            }
        }
    }

    private void setClasspath() {
        String runtimeClasspath = ((SourceSetContainer) properties.get("sourceSets")).getByName("test").getRuntimeClasspath().getAsPath();
        extension.setClasspath(runtimeClasspath);
    }

    private void setGaugeRoot() {
        String gaugeRoot = (String) properties.get(GAUGE_ROOT);
        if (gaugeRoot != null) {
            extension.setGaugeRoot(gaugeRoot);
        }
    }

    private void findFiles(String dir, Set<String> classPaths) {
        File files = new File(dir);
        if (!files.exists()) {
            return;
        }
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                classPaths.add(file.getAbsolutePath());
                findFiles(file.getAbsolutePath(), classPaths);
            }
        }
    }
}
