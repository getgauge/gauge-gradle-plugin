package org.gauge.gradle;

import org.gradle.util.GradleVersion;

public enum TestGradleVersions {
    v7_6("7.6.6"), // Jul 2025
    v8_14("8.14.1"), // May 2025
    vCURRENT(GradleVersion.current().getVersion());

    final String ver;

    TestGradleVersions(String version) {
        this.ver = version;
    }

    @Override
    public String toString() {
        return ver;
    }
}
