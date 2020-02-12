package jira;

enum IssueId {
    TEST_PLAN(10103);

    IssueId(long id) {
        this.id = id;
    }

    private final long id;

    public long getId() {
        return id;
    }
}
