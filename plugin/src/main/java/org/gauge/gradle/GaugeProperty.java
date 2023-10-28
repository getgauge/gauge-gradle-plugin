package org.gauge.gradle;

enum GaugeProperty {

    ADDITIONAL_FLAGS("additionalFlags", ""),
    ENV("env", "--env"),
    GAUGE_ROOT("gaugeRoot", ""),
    TAGS("tags", "--tags"),
    SPECS_DIR("specsDir", ""),
    IN_PARALLEL("inParallel", "--parallel"),
    NODES("nodes", "--n"),
    PROJECT_DIR("dir", "--dir");

    private final String key;
    private final String flag;

    GaugeProperty(final String key, final String flag) {
        this.key = key;
        this.flag = flag;
    }

    String getKey() {
        return this.key;
    }

    String getFlag() {
        return this.flag;
    }

}
