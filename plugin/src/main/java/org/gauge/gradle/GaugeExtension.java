package org.gauge.gradle;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

public abstract class GaugeExtension {

    @Input
    @Optional
    public abstract Property<String> getDir();

    @Input
    @Optional
    public abstract Property<String> getEnv();

    @Input
    @Optional
    public abstract Property<String> getTags();

    @Input
    @Optional
    public abstract Property<String> getSpecsDir();

    @Input
    @Optional
    public abstract Property<Boolean> getInParallel();

    @Input
    @Optional
    public abstract Property<Integer> getNodes();

    @Input
    @Optional
    public abstract MapProperty<String, String> getEnvironmentVariables();

    @Input
    @Optional
    public abstract Property<String> getAdditionalFlags();

    @Input
    @Optional
    public abstract Property<String> getGaugeRoot();

}
