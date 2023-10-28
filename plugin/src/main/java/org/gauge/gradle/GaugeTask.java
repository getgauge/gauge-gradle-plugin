package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GaugeTask extends Test {
    private static final Logger logger = LoggerFactory.getLogger("gauge");

    public GaugeTask() {
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.setDescription("Runs the Gauge test suite.");
        // So that previous outputs of this task cannot be reused
        this.getOutputs().upToDateWhen(task -> false);
    }

    @TaskAction
    public void execute() {
        final Project project = getProject();
        final GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        final GaugeCommand command = new GaugeCommand(extension, project);

        project.exec(spec -> {
            // Usage:
            // gauge <command> [flags] [args]
            spec.executable(command.getExecutable());
            spec.args("run");
            spec.args(command.getProjectDir());
            spec.args(command.getFlags());
            if (command.isNotFailedOrRepeatFlagProvided()) {
                spec.args(command.getEnvironment());
                spec.args(command.getTags());
                spec.args(command.getSpecsDir());
            }
            spec.environment(GaugeConstants.GAUGE_CUSTOM_CLASSPATH, getClasspath().getAsPath());
            if (null != extension) {
                extension.getEnvironmentVariables().get().forEach(spec::environment);
            }
            logger.info("Running {} {}", spec.getExecutable(), spec.getArgs());
        });
    }

}
