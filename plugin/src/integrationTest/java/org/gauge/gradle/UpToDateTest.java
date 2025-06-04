package org.gauge.gradle;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

public class UpToDateTest extends Base {

    @Test
    void testGaugeTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments(GAUGE_TASK);
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
    }

    @Test
    void testGaugeValidateTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments("gaugeValidate");
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
    }

    @Test
    void testGaugeClasspathTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments(GAUGE_CLASSPATH_TASK);
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
    }

}
