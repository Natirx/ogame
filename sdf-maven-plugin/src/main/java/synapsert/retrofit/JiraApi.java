package synapsert.retrofit;

import retrofit2.Call;
import retrofit2.http.*;
import synapsert.models.NewCycle;
import synapsert.models.ExistingCycle;
import synapsert.models.TestCaseKeys;
import synapsert.models.TestRunUpdate;

import java.util.List;

public interface JiraApi {

    @POST("/rest/synapse/latest/public/testPlan/{testPlanIssueKey}/addCycle")
    Call<Void> addCycle(@Path("testPlanIssueKey") String testPlanIssueKey, @Body NewCycle cycle);

    @GET("/rest/synapse/latest/public/testPlan/{testPlanIssueKey}/cycles")
    Call<List<ExistingCycle>> getCycles(@Path("testPlanIssueKey") String testPlanIssueKey);

    @POST("/rest/synapse/latest/public/testPlan/{testPlanIssueKey}/addMembers")
    Call<Void> addTestCasesToTestPlan(@Path("testPlanIssueKey") String testPlanIssueKey, @Body TestCaseKeys testCaseKeyIds);

    @PUT("/rest/synapse/latest/public/testPlan/{testPlanIssueKey}/cycle/{cycleName}/wf/{action}")
    Call<Void> updateTestCycleStatus(@Path("testPlanIssueKey") String testPlanIssueKey, @Path("cycleName") String cycleName, @Path("action") String action);

    @POST("/rest/synapse/latest/public/testPlan/{testPlanIssueKey}/cycle/{cycleName}/updateTestRun")
    Call<Void> updateTestRun(@Path("testPlanIssueKey") String testPlanIssueKey, @Path("cycleName") String cycleName, @Body TestRunUpdate testRunUpdate);

}
