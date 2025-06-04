package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecSpec;

public abstract class GaugeValidateTask extends AbstractGaugeTask {

    @Inject
    public GaugeValidateTask(final ExecOperations execOps, final Project project) {
        super(execOps, project);
        this.setDescription("Check for validation and parse errors.");
    }

    @Override
    protected void configureSpec(final ExecSpec spec, final GaugeCommand command) {
        spec.args("validate");
        spec.args(command.getProjectDir());
        spec.args(command.getEnvironment());
        spec.args(command.getSpecsDir());
    }

}
