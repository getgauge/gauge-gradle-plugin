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

/**
 * Abstract base class for Gauge tasks, providing common functionality for Gauge-related Gradle tasks.
 */
@DisableCachingByDefault
public abstract class AbstractGaugeTask extends DefaultTask {
    /**
     * Logger instance for Gauge tasks.
     */
    protected static final Logger logger = LoggerFactory.getLogger("gauge");
    /**
     * ExecOperations for executing external processes.
     */
    protected final ExecOperations execOps;
    /**
     * The Gradle project associated with this task.
     */
    protected final Project project;
    /**
     * Constructs an AbstractGaugeTask.
     *
     * @param execOps     the ExecOperations instance
     * @param project     the Gradle project
     * @param description the task description
     */
    @Inject
    protected AbstractGaugeTask(final ExecOperations execOps, final Project project, final String description) {
        this.execOps = execOps;
        this.project = project;
        super.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        super.setDescription(description);
        super.dependsOn("classes", "testClasses");
    }
    /**
     * Configures the ExecSpec for the Gauge command.
     *
     * @param spec    the ExecSpec to configure
     * @param command the GaugeCommand instance
     */
    protected abstract void configureSpec(final ExecSpec spec, final GaugeCommand command);

    private String getClasspath() {
        return project.getExtensions().getByType(SourceSetContainer.class)
            .getByName("test")
            .getRuntimeClasspath()
            .getAsPath();
    }

    /**
     * Executes the Gauge task.
     */
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
