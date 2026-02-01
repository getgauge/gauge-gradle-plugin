package org.gauge.gradle;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

/**
 * Extension for configuring Gauge plugin properties in Gradle.
 */
public abstract class GaugeExtension {
    /**
     * Default constructor for GaugeExtension.
     */
    public GaugeExtension() {
        super();
    }

    /**
     * Returns the project directory property.
     *
     * @return the project directory property
     */
    @Input
    @Optional
    public abstract Property<String> getDir();
    /**
     * Returns the environment property.
     *
     * @return the environment property
     */
    @Input
    @Optional
    public abstract Property<String> getEnv();
    /**
     * Returns the tags property.
     *
     * @return the tags property
     */
    @Input
    @Optional
    public abstract Property<String> getTags();
    /**
     * Returns the specs directory property.
     *
     * @return the specs directory property
     */
    @Input
    @Optional
    public abstract Property<String> getSpecsDir();
    /**
     * Returns the in-parallel property.
     *
     * @return the in-parallel property
     */
    @Input
    @Optional
    public abstract Property<Boolean> getInParallel();
    /**
     * Returns the nodes property.
     *
     * @return the nodes property
     */
    @Input
    @Optional
    public abstract Property<Integer> getNodes();
    /**
     * Returns the environment variables property.
     *
     * @return the environment variables property
     */
    @Input
    @Optional
    public abstract MapProperty<String, String> getEnvironmentVariables();
    /**
     * Returns the additional flags property.
     *
     * @return the additional flags property
     */
    @Input
    @Optional
    public abstract Property<String> getAdditionalFlags();
    /**
     * Returns the Gauge root property.
     *
     * @return the Gauge root property
     */
    @Input
    @Optional
    public abstract Property<String> getGaugeRoot();
}
