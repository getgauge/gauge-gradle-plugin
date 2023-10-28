package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gauge.gradle.GaugeConstants.GAUGE_VALIDATE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateTest extends Base {

    @Test
    void testCanRunGaugeValidateTask() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied without gauge extension
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the classpath task
        BuildResult result = defaultGradleRunner().withArguments(GAUGE_VALIDATE_TASK).build();
        // And I should see no validation errors
        assertEquals(SUCCESS, result.task(":" + GAUGE_VALIDATE_TASK).getOutcome());
        assertThat(result.getOutput(), containsString("No errors found."));
        // When specsDir is set in extension
        // And example4.spec contains a missing step implementation
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {specsDir=\"multipleSpecs/example4.spec\"}\n");
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_VALIDATE_TASK).buildAndFail();
        // Then I should see a failure with a validation error
        assertEquals(FAILED, resultWithExtension.task(":" + GAUGE_VALIDATE_TASK).getOutcome());
        assertThat(resultWithExtension.getOutput(), containsString("example4.spec:6 Step implementation not found"));
    }

}
