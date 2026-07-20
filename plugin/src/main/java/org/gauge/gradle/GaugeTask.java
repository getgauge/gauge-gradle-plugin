package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecSpec;

/**
 * Task to run the Gauge test suite.
 */
public abstract class GaugeTask extends AbstractGaugeTask {

    /**
     * Constructs a GaugeTask.
     *
     * @param execOps the ExecOperations instance
     * @param project the Gradle project
     */
    @Inject
    public GaugeTask(final ExecOperations execOps, final Project project) {
        super(execOps, project, "Runs the Gauge test suite.");
    }

    /**
     * Configures the ExecSpec for running Gauge tests.
     *
     * @param spec    the ExecSpec to configure
     * @param command the GaugeCommand instance
     */
    @Override
    protected void configureSpec(final ExecSpec spec, final GaugeCommand command) {
        spec.args("run");
        spec.args(command.getProjectDir());
        spec.args(command.getFlags());
        if (command.isNotFailedOrRepeatFlagProvided()) {
            spec.args(command.getEnvironment());
            spec.args(command.getTags());
            spec.args(command.getSpecsDir());
        }
    }

}
