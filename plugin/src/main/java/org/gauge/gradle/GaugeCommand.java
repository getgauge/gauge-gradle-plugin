package org.gauge.gradle;

import org.gradle.api.Project;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a Gauge command and provides methods to construct command-line arguments for Gauge execution.
 */
public class GaugeCommand {

    private final GaugeExtension extension;
    private final Map<String, ?> properties;
    private final Project project;

    /**
     * Constructs a GaugeCommand instance.
     *
     * @param extension the GaugeExtension instance
     * @param project   the Gradle project
     */
    public GaugeCommand(final GaugeExtension extension, final Project project) {
        this.extension = extension;
        this.project = project;
        this.properties = project.getProperties();
    }

    /**
     * Returns the Gauge executable path.
     *
     * @return the path to the Gauge executable
     */
    public String getExecutable() {
        final String binary = "gauge";
        return project.hasProperty(GaugeProperty.GAUGE_ROOT.getKey())
                ? getExecutablePath(properties.get(GaugeProperty.GAUGE_ROOT.getKey()).toString()).toString()
                : extension.getGaugeRoot().isPresent()
                    ? getExecutablePath(extension.getGaugeRoot().get()).toString()
                    : binary;
    }

    private Path getExecutablePath(final String gaugeRoot) {
        return Paths.get(gaugeRoot, "bin", "gauge");
    }

    /**
     * Returns the project directory argument for Gauge.
     *
     * @return a list containing the project directory flag and value
     */
    public List<String> getProjectDir() {
        return List.of(GaugeProperty.PROJECT_DIR.getFlag(), getDir());
    }

    private String getDir() {
        return project.hasProperty(GaugeProperty.PROJECT_DIR.getKey())
                ? getProjectPath(properties.get(GaugeProperty.PROJECT_DIR.getKey()).toString()).toString()
                : getProjectPath(extension.getDir().getOrElse(project.getProjectDir().getAbsolutePath())).toString();
    }

    private Path getProjectPath(final String projectDir) {
        return project.getProjectDir().toPath().resolve(Path.of(projectDir)).toAbsolutePath();
    }

    /**
     * Returns the environment argument for Gauge.
     *
     * @return a list containing the environment flag and value
     */
    public List<String> getEnvironment() {
        return List.of(GaugeProperty.ENV.getFlag(), getEnv().trim());
    }

    private String getEnv() {
        return project.hasProperty(GaugeProperty.ENV.getKey())
                ? properties.get(GaugeProperty.ENV.getKey()).toString()
                : extension.getEnv().get();
    }

    /**
     * Returns true if neither --failed nor --repeat flags are provided.
     *
     * @return true if neither flag is present, false otherwise
     */
    public boolean isNotFailedOrRepeatFlagProvided() {
        final List<String> flags = getAdditionalFlags();
        return !flags.contains("--failed") && !flags.contains("--repeat");
    }

    /**
     * Returns the list of flags for Gauge execution.
     *
     * @return a list of flags
     */
    public List<Object> getFlags() {
        final List<Object> flags = new ArrayList<>(getAdditionalFlags());
        // --repeat and --failed flags cannot be run with other flags
        if (isNotFailedOrRepeatFlagProvided() && isInParallel()) {
            flags.add(GaugeProperty.IN_PARALLEL.getFlag());
            final int nodes = getNodes();
            if (nodes != 0) {
                flags.addAll(List.of(GaugeProperty.NODES.getFlag(), nodes));
            }
        }
        return flags;
    }

    private List<String> getAdditionalFlags() {
        return project.hasProperty(GaugeProperty.ADDITIONAL_FLAGS.getKey())
                ? getListFromString(properties.get(GaugeProperty.ADDITIONAL_FLAGS.getKey()).toString())
                : extension.getAdditionalFlags().isPresent()
                ? getListFromString(extension.getAdditionalFlags().get())
                : Collections.emptyList();
    }

    private List<String> getListFromString(final String value) {
        return Arrays.stream(value.split("\\s+")).map(String::trim).toList();
    }

    private int getNodes() {
        return project.hasProperty(GaugeProperty.NODES.getKey())
                ? Integer.parseInt(properties.get(GaugeProperty.NODES.getKey()).toString())
                : extension.getNodes().isPresent() ? extension.getNodes().get() : 0;
    }

    private boolean isInParallel() {
        return project.hasProperty(GaugeProperty.IN_PARALLEL.getKey())
                ? Boolean.parseBoolean(project.getProperties().get(GaugeProperty.IN_PARALLEL.getKey()).toString())
                : extension.getInParallel().get();
    }

    /**
     * Returns the specs directory argument for Gauge.
     *
     * @return a list containing the specs directory flag and value
     */
    public List<String> getSpecsDir() {
        final var specs = properties.containsKey(GaugeProperty.SPECS_DIR.getKey())
                ? properties.get(GaugeProperty.SPECS_DIR.getKey()).toString() : extension.getSpecsDir().get();
        return getListFromString(specs.trim());
    }

    /**
     * Returns the tags argument for Gauge.
     *
     * @return a list containing the tags flag and value, or an empty list if not set
     */
    public List<String> getTags() {
        final String tags = project.hasProperty(GaugeProperty.TAGS.getKey())
                ? properties.get(GaugeProperty.TAGS.getKey()).toString()
                : extension.getTags().isPresent() ? extension.getTags().get() : "";
        return !tags.isEmpty() ? List.of(GaugeProperty.TAGS.getFlag(), tags) : Collections.emptyList();
    }

}
