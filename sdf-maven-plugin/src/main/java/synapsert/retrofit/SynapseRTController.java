package synapsert.retrofit;

import synapsert.models.*;

import java.util.List;

public class SynapseRTController {

    public static void createTestCycle(String testPlanName, NewCycle cycle) {
        RetrofitCallAdapter.execute(JiraClient.get().addCycle(testPlanName, cycle));
    }

    public static List<ExistingCycle> getCycles(String testPlanName) {
        return RetrofitCallAdapter.execute(JiraClient.get().getCycles(testPlanName)).body();
    }

    public static void addTestCasesToTestPlan(String testPlanName, List<String> testCaseKeyIds) {
        RetrofitCallAdapter.execute(JiraClient.get().addTestCasesToTestPlan(testPlanName, TestCaseKeys.builder().testCaseKeys(testCaseKeyIds).build()));
    }

    public static void updateTestCycleStatus(String testPlanName, String testCycleName, TestCycleStatus testCycleStatus) {
        RetrofitCallAdapter.execute(JiraClient.get().updateTestCycleStatus(testPlanName, testCycleName, testCycleStatus.getTestCycleStatus()));
    }

    public static void updateTestRun(String testPlanName, String testCycleName, TestRunUpdate testRunUpdate) {
        RetrofitCallAdapter.execute(JiraClient.get().updateTestRun(testPlanName, testCycleName, testRunUpdate));
    }
}