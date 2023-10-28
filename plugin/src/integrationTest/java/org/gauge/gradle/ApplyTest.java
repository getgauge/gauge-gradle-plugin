package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplyTest extends Base {

    @Test
    void testCanApplyPlugin() throws IOException {
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult result = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(NO_SOURCE, result.task(GAUGE_TASK_PATH).getOutcome());
    }

}
