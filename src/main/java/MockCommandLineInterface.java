public class MockCommandLineInterface implements ICommandLineInterface {
    private boolean isRunning = false;
    private String branch = "";

    public void exec(String commands) {
        this.isRunning = true;
        if (commands.contains("noLintNeededBranch")) {
            branch = "noLintNeededBranch";
        } else if (commands.contains("linterRequiresFurtherAssistanceBranch")) {
            branch = "linterRequiresFurtherAssistanceBranch";
        } else if (commands.contains("lintAutoFixBranch")) {
            branch = "lintAutoFixBranch";
        } else {
            return;
        }
    }

    public IBufferedReader getInputStream() {
        if (branch == "noLintNeededBranch") {
            return new MockBufferedReader(0);
        } else if (branch == "linterRequiresFurtherAssistanceBranch") {
            return new MockBufferedReader(1);
        } else if (branch == "lintAutoFixBranch") {
            return new MockBufferedReader(2);
        } else {
            return new MockBufferedReader(3);
        }
    }
}
