package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GaugeValidateTask extends GaugeTask {
    private static final Logger logger = LoggerFactory.getLogger("gauge");

    public GaugeValidateTask() {
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.setDescription("Check for validation and parse errors.");
        // So that previous outputs of this task cannot be reused
        this.getOutputs().upToDateWhen(task -> false);
    }
    @TaskAction
    public void execute() {
        final Project project = getProject();
        final GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        final GaugeCommand command = new GaugeCommand(extension, project);
        project.exec(spec -> {
            spec.executable(command.getExecutable());
            spec.args("validate");
            spec.args(command.getProjectDir());
            spec.args(command.getSpecsDir());
            spec.environment(GaugeConstants.GAUGE_CUSTOM_CLASSPATH, getClasspath().getAsPath());
            if (null != extension) {
                extension.getEnvironmentVariables().get().forEach(spec::environment);
            }
            logger.info("Running {} {}", spec.getExecutable(), spec.getArgs());
        });
    }

}
