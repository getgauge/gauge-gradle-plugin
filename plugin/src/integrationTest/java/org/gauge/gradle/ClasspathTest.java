package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gauge.gradle.GaugeConstants.GAUGE_CLASSPATH_TASK;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClasspathTest extends Base {

    @Test
    void testCanRunGaugeClasspathTaskWithGaugeDependency() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied without gauge extension
        // And gauge-java dependency included
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the classpath task
        BuildResult result = defaultGradleRunner().withArguments(GAUGE_CLASSPATH_TASK).build();
        assertEquals(SUCCESS, result.task(":" + GAUGE_CLASSPATH_TASK).getOutcome());
        assertThat(result.getOutput(), containsString("gauge-java"));
        assertThat(result.getOutput(), containsString("assertj-core"));
    }

}
