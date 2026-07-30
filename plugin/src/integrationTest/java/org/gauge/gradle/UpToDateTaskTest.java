package org.gauge.gradle;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class UpToDateTaskTest extends Base {

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testGaugeTaskIsNotCached(GradleTestVersion gradle) throws IOException {
        copyGaugeFixtureToTemp();
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner(gradle).withArguments(GAUGE_TASK);
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
        assertEquals(SUCCESS, runner.build().task(GAUGE_TASK_PATH).getOutcome());
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testGaugeValidateTaskIsNotCached(GradleTestVersion gradle) throws IOException {
        copyGaugeFixtureToTemp();
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner(gradle).withArguments("gaugeValidate");
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testGaugeClasspathTaskIsNotCached(GradleTestVersion gradle) throws IOException {
        copyGaugeFixtureToTemp();
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner(gradle).withArguments(GAUGE_CLASSPATH_TASK);
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
        assertEquals(SUCCESS, runner.build().task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
    }

}
