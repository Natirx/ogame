package goals;

import data.Constants;
import jira.IssueStatus;
import jira.JiraApiInternal;
import jira.Jql;
import lombok.extern.java.Log;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import synapsert.TestStatusCollector;
import synapsert.models.*;
import synapsert.retrofit.SynapseRTController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static data.Constants.GITLAB_REPORT_LINK;

@Log
@Mojo(name = "synapse", defaultPhase = LifecyclePhase.COMPILE)
public class SynapseRT extends AbstractMojo {

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;
    @Parameter(property = "testStatusPath")
    private String testStatusPath;

    @Override
    public void execute() {
        if (!skip) {
            if (Constants.SPRINT_NUMBER == null) {
                throw new IllegalArgumentException("Sprint number is not provided");
            }
            final String testPlanSummary = "Americium: Automation - Sprint " + Constants.SPRINT_NUMBER;
            String testPlanKey = prepareTestPlan(testPlanSummary);
            NewCycle cycle = prepareCycle(testPlanKey);
            updateTestCasesResults(testPlanKey, cycle);
            completeCycle(testPlanKey, cycle);
        }
    }

    private void completeCycle(String testPlanKey, NewCycle cycle) {
        log.info("Completing '" + cycle.getName() + "' cycle");
        SynapseRTController.updateTestCycleStatus(testPlanKey, cycle.getName(), TestCycleStatus.COMPLETE);
    }

    private void updateTestCasesResults(String testPlanKey, NewCycle cycle) {
        TestStatusCollector.deserialize(testStatusPath).forEach((k, v) -> {
            TestRunUpdate testRunUpdate = TestRunUpdate.builder()
                    .testCaseKey(k)
                    .result(v)
                    .comment("Please find the report by following the next url: " + GITLAB_REPORT_LINK)
                    .build();
            log.info("Updating status of '" + k + "' to '" + v + "'");
            SynapseRTController.updateTestRun(testPlanKey, cycle.getName(), testRunUpdate);
        });
    }

    private NewCycle prepareCycle(String testPlanKey) {
        List<ExistingCycle> cycles = SynapseRTController.getCycles(testPlanKey);
        int nextCycle;
        if (cycles.size() == 0) {
            nextCycle = 1;
        } else {
            /**
             * Cycle name has the following format "Cycle_<number>"
             * */
            nextCycle = Integer.parseInt(cycles.get(cycles.size() - 1).getName().split("_")[1]) + 1;
        }
        String cycleName = "Cycle_" + nextCycle;
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        NewCycle cycle = NewCycle.builder()
                .name(cycleName)
                .environment("")
                .buildName("")
                .plannedStartDate(date)
                .plannedEndDate(date)
                .build();
        log.info("Creating '" + cycleName + "' cycle");
        SynapseRTController.createTestCycle(testPlanKey, cycle);
        SynapseRTController.updateTestCycleStatus(testPlanKey, cycle.getName(), TestCycleStatus.START);
        return cycle;
    }

    private String prepareTestPlan(String testPlanSummary) {
        JiraApiInternal client = new JiraApiInternal();
        String key;
        if (client.isIssueBySummary(testPlanSummary)) {
            /**
             * If test plan exists then check the status and perform an appropriate action
             * */
            log.info("'" + testPlanSummary + "' test plan is already created");
            key = client.getIssueKeyBySummary(testPlanSummary);
            String status = client.getIssueStatus(key);
            if (status.equals(IssueStatus.TODO.getStatus())) {
                client.issueStartProgress(key);
                List<String> automatedTestCases = client.searchIssues(Jql.AUTOMATED_TEST_CASES);
                SynapseRTController.addTestCasesToTestPlan(key, automatedTestCases);
            } else if (status.equals(IssueStatus.IN_PROGRESS.getStatus())) {
                //everything is fine
            } else if (status.equals(IssueStatus.DONE.getStatus())) {
                throw new RuntimeException("'" + testPlanSummary + "' test plan is already in 'Done' status");
            } else {
                throw new RuntimeException("There is no implementation for '" + status + "' status");
            }
        } else {
            /**
             * If test plan doesn't exist then create, start progress and add the test cases
             * */
            log.info("Creating '" + testPlanSummary + "' test plan");
            key = client.createTestPlan(testPlanSummary);
            client.issueStartProgress(key);
            List<String> automatedTestCases = client.searchIssues(Jql.AUTOMATED_TEST_CASES);
            SynapseRTController.addTestCasesToTestPlan(key, automatedTestCases);
        }
        return key;
    }
}