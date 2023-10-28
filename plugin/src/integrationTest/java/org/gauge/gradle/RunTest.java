package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunTest extends Base {

    private static final String GAUGE_PROJECT_ONE = "project1";

    @BeforeEach
    public void setUp() {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
    }

    @Test
    void testCanRunGaugeTasksWithDefaultConfigurations() throws IOException {
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult result = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, result.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(result.getOutput(), containsString("Successfully generated html-report"));
    }

    @Test
    void testCanRunGaugeTestsWhenDirPropertySet() throws IOException {
        final File subProject = new File(Path.of(defaultGradleRunner().getProjectDir().getPath(), "subProject").toString());
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE, subProject);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult resultWithDirProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Pdir=" + subProject.getAbsolutePath()).build();
        assertEquals(SUCCESS, resultWithDirProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @Test
    void testCanRunGaugeTestsWhenDirSetInExtension() throws IOException {
        final File subProject = new File(Path.of(defaultGradleRunner().getProjectDir().getPath(), "subProject").toString());
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE, subProject);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {dir=\"subProject\"}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtensionProperty = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtensionProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @Test
    void testCanRunGaugeTestsWhenSpecsDirSet() throws IOException {
        // Given plugin is applied
        // When specsDir is set in the extension with an invalid/non-existing directory
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {specsDir=\"invalid\"}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithExtension.getOutput(), containsString("Specs directory invalid does not exist."));
        // When specsDir is set to multiple specs directory with one being an invalid/non-existing directory
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-PspecsDir=specs specs2").buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithProperty.getOutput(), containsString("Specs directory specs2 does not exist."));
    }

    @Test
    void testCanRunGaugeTestsWhenEnvVariablesAndAdditionalFlagsSet() throws IOException {
        // Given plugin is applied
        // When environmentVariables is set in extension
        // And additionalFlags include the --verbose flag
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {environmentVariables=['customVariable': 'customValue']\n"
                + "additionalFlags='--simple-console --verbose'}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see custom environment was set correctly
        assertThat(resultWithExtension.getOutput(), containsString("customVariable is set to customValue in build.gradle"));
        // And I should see the step names included in console output with --verbose flag set
        assertThat(resultWithExtension.getOutput(), containsString("The word \"gauge\" has \"3\" vowels."));
    }

    @Test
    void testCanRunGaugeTestsWhenInParallelSet() throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {specsDir='specs multipleSpecs'\n"
                + "inParallel=true\n"
                + "additionalFlags='--simple-console'}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests running in default parallel streams
        assertThat(resultWithExtension.getOutput(), containsString("parallel streams."));
        // And I should see all 4 specifications were executed
        assertThat(resultWithExtension.getOutput(), containsString("Specifications:\t4 executed"));
        // When nodes=2 project property is set
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Pnodes=2").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should see tests running in 2 parallel streams
        assertThat(resultWithProperty.getOutput(), containsString("Executing in 2 parallel streams."));
        // And I should see all 4 specifications were executed
        assertThat(resultWithProperty.getOutput(), containsString("Specifications:\t4 executed"));
    }

    @Test
    void testCanRunGaugeTestsWhenTagsSet() throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        // And tags=example1 set to run
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {specsDir='specs multipleSpecs'\n"
                + "inParallel=true\n"
                + "additionalFlags='--simple-console'\n"
                + "tags='example1'}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests running only with specified tag
        assertThat(resultWithExtension.getOutput(), containsString("parallel streams."));
        assertThat(resultWithExtension.getOutput(), containsString("Specifications:\t2 executed"));
        // When nodes=2 project property is set
        // And tags project property is set to run either scenarios with example1 or example2 tags
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Pnodes=2", "-Ptags=example1|example2").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should see tests running in 2 parallel streams
        assertThat(resultWithProperty.getOutput(), containsString("Executing in 2 parallel streams."));
        // And I should see all matching 3 specifications were executed
        assertThat(resultWithProperty.getOutput(), containsString("Specifications:\t3 executed"));
    }

    @Test
    void testCanRunGaugeTestsWhenEnvSet() throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        // When env is set to invalid/non-existing
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {inParallel=true\n"
                + "additionalFlags='--simple-console'\n"
                + "env='invalid'}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).buildAndFail();
        assertEquals(FAILED, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see environment does not exist error
        assertThat(resultWithExtension.getOutput(), containsString("invalid environment does not exist"));
        // When env=dev project property is set
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Penv=dev").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests ran against the dev environment
        assertThat(resultWithProperty.getOutput(), containsString(getExpectedReportPath("dev")));
    }

    @Test
    void testCanRunGaugeTestsWhenRepeatFlagSet() throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        // When env is set to dev
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {inParallel=true\n"
                + "additionalFlags='--simple-console'\n"
                + "nodes=2\n"
                + "env='dev'}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK, "--info").build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see environment and parallel flags with specs in the command
        assertThat(resultWithExtension.getOutput(), containsString("--simple-console --parallel --n 2 --env dev specs"));
        // When additionalFlags include the --repeat flag
        BuildResult resultWithProperty = defaultGradleRunner()
                .withArguments(GAUGE_TASK, "-PadditionalFlags=--repeat --simple-console", "--info").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should not see environment and parallel flags and specs include the command
        assertThat(resultWithProperty.getOutput(), not(containsString("--parallel --n 2 --env dev specs")));
        // And I should only see repeat and simple-console included
        assertThat(resultWithProperty.getOutput(), containsString("--repeat --simple-console"));
        // And I should see tests ran against the dev environment
        assertThat(resultWithProperty.getOutput(), containsString(getExpectedReportPath("dev")));
    }

    private String getExpectedReportPath(final String env) {
        return Path.of("reports", env, "html-report", "index.html").toString();
    }

}
