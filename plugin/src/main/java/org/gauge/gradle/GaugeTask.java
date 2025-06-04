package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecSpec;

public abstract class GaugeTask extends AbstractGaugeTask {

    @Inject
    public GaugeTask(final ExecOperations execOps, final Project project) {
        super(execOps, project);
        this.setDescription("Runs the Gauge test suite.");
    }

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
