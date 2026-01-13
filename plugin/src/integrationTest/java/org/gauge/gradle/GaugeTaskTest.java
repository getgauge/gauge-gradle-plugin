package org.gauge.gradle;

import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class GaugeTaskTest extends Base {

    @BeforeEach
    void setUp() throws IOException {
        copyGaugeFixtureToTemp();
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTasksWithDefaultConfigurations(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult result = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, result.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(result.getOutput(), containsString("Successfully generated html-report"));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenDirPropertySet(GradleTestVersion gradle) throws IOException {
        final File subProject = new File(Path.of(defaultGradleRunner(gradle).getProjectDir().getPath(), "subProject").toString());
        copyGaugeFixtureToTemp(subProject, "simple-project");
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult resultWithDirProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "-Pdir=" + subProject.getAbsolutePath()).build();
        assertEquals(SUCCESS, resultWithDirProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenDirSetInExtension(GradleTestVersion gradle) throws IOException {
        final File subProject = new File(Path.of(defaultGradleRunner(gradle).getProjectDir().getPath(), "subProject").toString());
        copyGaugeFixtureToTemp(subProject, "simple-project");
        // Given plugin is applied
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  dir = "subProject"
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtensionProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtensionProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenSpecsDirSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When specsDir is set in the extension with an invalid/non-existing directory
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  specsDir= "invalid"
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithExtension.getOutput(), containsString("Specs directory invalid does not exist."));
        // When specsDir is set to multiple specs directory with one being an invalid/non-existing directory
        BuildResult resultWithProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "-PspecsDir=specs specs2").buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithProperty.getOutput(), containsString("Specs directory specs2 does not exist."));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenEnvVariablesAndAdditionalFlagsSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When environmentVariables is set in extension
        // And additionalFlags include the --verbose flag
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  environmentVariables = ['customVariable': 'customValue']
                  additionalFlags = '--simple-console --verbose'
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see custom environment was set correctly
        assertThat(resultWithExtension.getOutput(), containsString("customVariable is set to customValue in build.gradle"));
        // And I should see the step names included in console output with --verbose flag set
        assertThat(resultWithExtension.getOutput(), containsString("The word \"gauge\" has \"3\" vowels."));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenInParallelSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --simple-console flag
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  specsDir = 'specs multipleSpecs'
                  inParallel = true
                  nodes = 2
                  additionalFlags = '--simple-console --verbose'
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests running in default parallel streams
        assertThat(resultWithExtension.getOutput(), containsString("Executing in 2 parallel streams."));
        // And I should see all 4 specifications were executed
        assertThat(resultWithExtension.getOutput(), containsString("Specifications:\t4 executed"));
        // When nodes=3 project property is set
        BuildResult resultWithProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "-Pnodes=3").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should see tests running in 2 parallel streams
        assertThat(resultWithProperty.getOutput(), containsString("Executing in 3 parallel streams."));
        // And I should see all 4 specifications were executed
        assertThat(resultWithProperty.getOutput(), containsString("Specifications:\t4 executed"));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenTagsSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --simple-console flag
        // And tags=example1 set to run
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  specsDir='specs multipleSpecs'
                  inParallel = true
                  additionalFlags = '--simple-console --verbose'
                  tags = 'example1'
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests running only with specified tag
        assertThat(resultWithExtension.getOutput(), containsString("parallel streams."));
        assertThat(resultWithExtension.getOutput(), containsString("Specifications:\t2 executed"));
        // When nodes=2 project property is set
        // And tags project property is set to run either scenarios with example1 or example2 tags
        BuildResult resultWithProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "-Pnodes=2", "-Ptags=example1|example2").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should see tests running in 2 parallel streams
        assertThat(resultWithProperty.getOutput(), containsString("Executing in 2 parallel streams."));
        // And I should see all matching 3 specifications were executed
        assertThat(resultWithProperty.getOutput(), containsString("Specifications:\t3 executed"));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenEnvSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        // When env is set to invalid/non-existing
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  inParallel = true
                  additionalFlags = '--simple-console --verbose'
                  env = 'invalid'
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK).buildAndFail();
        assertEquals(FAILED, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see environment does not exist error
        assertThat(resultWithExtension.getOutput(), containsString("invalid environment does not exist"));
        // When env=dev project property is set
        BuildResult resultWithProperty = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "-Penv=dev").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests ran against the dev environment
        assertThat(resultWithProperty.getOutput(), containsString(getExpectedReportPath("dev")));
    }

    @ParameterizedTest
    @MethodSource(SUPPORTED_GRADLE_VERSIONS_FOR_CURRENT_JVM)
    void testCanRunGaugeTestsWhenRepeatFlagSet(GradleTestVersion gradle) throws IOException {
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --simple-console flag
        // When env is set to dev
        writeFile(buildFilePath, getApplyPluginsBlock() + """
                gauge {
                  additionalFlags = '--simple-console --verbose'
                  env = 'dev'
                }
                """);
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner(gradle).withArguments(GAUGE_TASK, "--info").build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see environment and parallel flags with specs in the command
        assertThat(resultWithExtension.getOutput(), containsString("--simple-console --verbose --env dev specs"));
        // When additionalFlags include the --repeat flag
        BuildResult resultWithProperty = defaultGradleRunner(gradle)
                .withArguments(GAUGE_TASK, "-PadditionalFlags=--repeat --simple-console --verbose", "--info").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should not see environment and parallel flags and specs include the command
        assertThat(resultWithProperty.getOutput(), not(containsString("--env dev specs")));
        // And I should only see repeat and simple-console included
        assertThat(resultWithProperty.getOutput(), containsString("--repeat --simple-console --verbose"));
        // And I should see tests ran against the dev environment
        assertThat(resultWithProperty.getOutput(), containsString(getExpectedReportPath("dev")));
    }

    private String getExpectedReportPath(final String env) {
        return Path.of("reports", env, "html-report", "index.html").toString();
    }
}

