package jira;

public enum Jql {
    AUTOMATED_TEST_CASES("project = Element AND type = \"Test Case\" AND component in (\"Submissions Management\") AND labels in (Regression) AND labels in (Automated)");

    private String jql;

    Jql(String jql) {
        this.jql = jql;
    }

    public String getJql() {
        return jql;
    }
}