package org.gauge.gradle;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;

public abstract class GaugeExtension {
    @Inject
    public GaugeExtension() {
        getEnv().convention(gradleProperty(GaugeProperty.ENV.getKey()).getOrElse("default"));
        getSpecsDir().convention(gradleProperty(GaugeProperty.SPECS_DIR.getKey()).getOrElse("specs"));
        getInParallel().convention(gradleProperty(GaugeProperty.IN_PARALLEL.getKey()).map(Boolean::parseBoolean).getOrElse(false));
    }

    @Inject
    protected abstract ProviderFactory getProviders();
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

    private Provider<String> gradleProperty(String name) {
        return getProviders().gradleProperty(name);
    }

}
