package org.gauge.gradle;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class UpToDateTest extends Base {

    @ParameterizedTest
    @EnumSource(TestGradleVersions.class)
    void testGaugeTaskIsNotCached(TestGradleVersions gradle) throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withGradleVersion(gradle.ver).withArguments(GAUGE_TASK);
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
    }

    @ParameterizedTest
    @EnumSource(TestGradleVersions.class)
    void testGaugeValidateTaskIsNotCached(TestGradleVersions gradle) throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withGradleVersion(gradle.ver).withArguments("gaugeValidate");
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
    }

    @ParameterizedTest
    @EnumSource(TestGradleVersions.class)
    void testGaugeClasspathTaskIsNotCached(TestGradleVersions gradle) throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withGradleVersion(gradle.ver).withArguments(GAUGE_CLASSPATH_TASK);
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
    }

}
