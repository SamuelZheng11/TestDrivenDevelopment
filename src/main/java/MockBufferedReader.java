import java.util.LinkedList;

public class MockBufferedReader implements IBufferedReader {
    private int type;
    private LinkedList<String> inputStreamNoError = new LinkedList<String>();
    private LinkedList<String> inputStreamErrorRequiresFurtherHelp = new LinkedList<String>();
    private LinkedList<String> inputStreamErrorAutoFix = new LinkedList<String>();

    public MockBufferedReader(int type) {
        // typed used to represent if this is an input or error buffer reader
        this.type = type;

        inputStreamNoError.add("Linter completed with no linting errors");

        inputStreamErrorAutoFix.add("Fixed 2 error(s) in app/misc/repo.ts");
        inputStreamErrorAutoFix.add("All linting errors resolved");

        inputStreamErrorRequiresFurtherHelp.add("ERROR: app/misc/router.ts:116:1 - non-arrow functions are forbidden");
    }

    public String readLine() {
        if (type == 0) {
            if (inputStreamNoError.size() != 0) {
                String returnValue = inputStreamNoError.getFirst();
                inputStreamNoError.removeFirst();
                return returnValue;
            } else {
                return null;
            }

        } else if (type == 1) {
            if (inputStreamErrorRequiresFurtherHelp.size() != 0) {
                String returnValue = inputStreamErrorRequiresFurtherHelp.getFirst();
                inputStreamErrorRequiresFurtherHelp.removeFirst();
                return returnValue;
            } else {
                return null;
            }

        } else if (type == 2) {
            if (inputStreamErrorAutoFix.size() != 0) {
                String returnValue = inputStreamErrorAutoFix.getFirst();
                inputStreamErrorAutoFix.removeFirst();
                return returnValue;
            } else {
                return null;
            }

        } else {
            return "";
        }
    }
}
