package org.gauge.gradle;

import org.gradle.util.GradleVersion;

import java.util.Arrays;

public enum GradleTestVersion {
    v7_6("7.6.6", 8, 19), // Jul 2025
    v8_14("8.14.5", 8, 25), // May 2026
    vCURRENT(GradleVersion.current().getVersion(), 17, 26);

    final String version;
    final int minJdk;
    final int maxJdk;

    GradleTestVersion(String version, int minJdk, int maxJdk) {
        this.version = version;
        this.minJdk = minJdk;
        this.maxJdk = maxJdk;
    }

    boolean isSupportedOnCurrentJvm() {
        int current = Runtime.version().feature();
        return minJdk <= current && maxJdk >= current;
    }

    boolean isAtLeast(int majorVersion) {
        return Integer.parseInt(version.split("\\.")[0]) >= majorVersion;
    }

    static GradleTestVersion[] supportedVersionsForCurrentJvm() {
        return Arrays.stream(values()).filter(GradleTestVersion::isSupportedOnCurrentJvm).toArray(GradleTestVersion[]::new);
    }

    static GradleTestVersion[] supportedVersionsForCurrentJvmFrom(int gradleMajorVersion) {
        return Arrays.stream(supportedVersionsForCurrentJvm()).filter(it -> it.isAtLeast(gradleMajorVersion)).toArray(GradleTestVersion[]::new);
    }

    @Override
    public String toString() {
        return version;
    }
}
