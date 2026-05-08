package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecSpec;

/**
 * Task to validate Gauge specifications for errors.
 */
public abstract class GaugeValidateTask extends AbstractGaugeTask {

    /**
     * Constructs a GaugeValidateTask.
     *
     * @param execOps the ExecOperations instance
     * @param project the Gradle project
     */
    @Inject
    public GaugeValidateTask(final ExecOperations execOps, final Project project) {
        super(execOps, project, "Check for validation and parse errors.");
    }

    /**
     * Configures the ExecSpec for validating Gauge specifications.
     *
     * @param spec    the ExecSpec to configure
     * @param command the GaugeCommand instance
     */
    @Override
    protected void configureSpec(final ExecSpec spec, final GaugeCommand command) {
        spec.args("validate");
        spec.args(command.getProjectDir());
        spec.args(command.getEnvironment());
        spec.args(command.getSpecsDir());
    }

}
