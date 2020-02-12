package jira;

public enum IssueStatus {
    IN_PROGRESS("In Progress"), DONE("Done"), TODO("To Do");

    private String status;

    IssueStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
