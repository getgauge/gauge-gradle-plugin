/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle;

import java.util.HashMap;
import java.util.Map;

public class GaugeExtension {

    private String specsDir;
    private Boolean inParallel = false;
    private Integer nodes;
    private String env;
    private String tags;
    private String classpath;
    private String additionalFlags;
    private String gaugeRoot;
    private Map<String, String> environmentVariables = new HashMap<>();

    public String getSpecsDir() {
        return specsDir;
    }

    public void setSpecsDir(String specsDir) {
        this.specsDir = specsDir;
    }

    public Boolean isInParallel() {
        return inParallel;
    }

    public void setInParallel(Boolean inParallel) {
        this.inParallel = inParallel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getAdditionalFlags() {
        return additionalFlags;
    }

    public void setAdditionalFlags(String additionalFlags) {
        this.additionalFlags = additionalFlags;
    }

    public String getGaugeRoot() {
        return gaugeRoot;
    }

    public void setGaugeRoot(String gaugeRoot) {
        this.gaugeRoot = gaugeRoot;
    }

    Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void environment(String key, String value) {
        this.environmentVariables.put(key, value);
    }
}
