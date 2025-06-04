package org.gauge.gradle;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecSpec;
import org.gradle.work.DisableCachingByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisableCachingByDefault
public abstract class AbstractGaugeTask extends DefaultTask {

    protected static final Logger logger = LoggerFactory.getLogger("gauge");
    protected final ExecOperations execOps;
    protected final Project project;

    @Inject
    public AbstractGaugeTask(final ExecOperations execOps, final Project project) {
        this.execOps = execOps;
        this.project = project;
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.dependsOn("build");
    }

    protected abstract void configureSpec(final ExecSpec spec, final GaugeCommand command);

    private String getClasspath() {
        return project.getExtensions().getByType(SourceSetContainer.class)
            .getByName("test")
            .getRuntimeClasspath()
            .getAsPath();
    }

    @TaskAction
    public void execute() {
        final GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        final GaugeCommand command = new GaugeCommand(extension, project);
        logger.info("Running gauge task: {}", this.getName());
        execOps.exec(spec -> {
            // Usage:
            // gauge <command> [flags] [args]
            spec.executable(command.getExecutable());
            configureSpec(spec, command);
            spec.environment(GaugeConstants.GAUGE_CUSTOM_CLASSPATH, getClasspath());
            if (null != extension) {
                extension.getEnvironmentVariables().get().forEach(spec::environment);
            }
            logger.info("Running {} {}", spec.getExecutable(), spec.getArgs());
        });
    }

}
