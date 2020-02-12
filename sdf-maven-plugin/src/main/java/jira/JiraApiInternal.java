package jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static data.Constants.*;

public class JiraApiInternal {
    private final JiraRestClient client;

    public JiraApiInternal() {
        client = new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(URI.create(JIRA_BASE_URL), JIRA_USERNAME, JIRA_PASS);
    }

    public String createTestPlan(String summary) {
        return client.getIssueClient()
                .createIssue(new IssueInputBuilder(PROJECT_KEY, IssueId.TEST_PLAN.getId(), summary).build())
                .claim().getKey();
    }

    private int getTransitionId(Issue issue, String transitionName) {
        Iterable<Transition> transitions = client.getIssueClient().getTransitions(issue).claim();
        return StreamSupport.stream(transitions.spliterator(), false)
                .filter(tr -> tr.getName().equals(transitionName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("'" + transitionName + "' transition is not available for " +
                        "'" + issue.getKey() + "' issue\nAll transitions:" + transitions))
                .getId();
    }

    public void addLabels(String issueKey, String... labels) {
        Set<String> set = client.getIssueClient().getIssue(issueKey).claim().getLabels();
        set.addAll(Arrays.asList(labels));
        client.getIssueClient().updateIssue(issueKey, new IssueInputBuilder().setFieldValue("labels", set).build()).claim();
    }

    public void issueStartProgress(String issueKey) {
        Issue issue = client.getIssueClient().getIssue(issueKey).claim();
        int id = getTransitionId(issue, "In Progress");
        client.getIssueClient().transition(issue, new TransitionInput(id, List.of())).claim();
    }

    public String getIssueStatus(String key) {
        return client.getIssueClient().getIssue(key).claim().getStatus().getName();
    }

    private List<Issue> getIssuesBySummary(String summary) {
        String jqlEscapingSpecialCharacter = "\\\\";
        summary = summary.replace(":", jqlEscapingSpecialCharacter + ":")
                .replace("-", jqlEscapingSpecialCharacter + "-");
        Iterable<Issue> issues = client.getSearchClient().searchJql("summary ~ \"" + summary + "\"").claim().getIssues();
        return StreamSupport.stream(issues.spliterator(), false).collect(Collectors.toList());
    }

    public boolean isIssueBySummary(String summary) {
        return getIssuesBySummary(summary).size() > 0;
    }

    public String getIssueKeyBySummary(String summary) {
        List<Issue> list = getIssuesBySummary(summary);
        if (list.size() > 1) {
            throw new RuntimeException("Were found more than 1 issue by the following JQL: summary ~ \"" + summary + "\"\n" +
                    "All found issues: " + list);
        } else if (list.size() == 0) {
            throw new RuntimeException("No issues were found by the following JQL: summary ~ \"" + summary + "\"");
        }
        return list.get(0).getKey();
    }

    public List<String> searchIssues(Jql jql) {
        Iterable<Issue> issues = client.getSearchClient().searchJql(jql.getJql(), 1000, 0, null).claim().getIssues();
        return StreamSupport.stream(issues.spliterator(), false)
                .map(BasicIssue::getKey)
                .collect(Collectors.toList());
    }
}
