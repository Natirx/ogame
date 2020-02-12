package synapsert.models;

public enum TestCycleStatus {
    START("Start"), COMPLETE("Complete"), ABORT("Abort"), RESUME("Resume");

    private final String testCycleStatus;

    TestCycleStatus(String testCycleStatus){
        this.testCycleStatus = testCycleStatus;
    }

    public String getTestCycleStatus(){
        return testCycleStatus;
    }
}