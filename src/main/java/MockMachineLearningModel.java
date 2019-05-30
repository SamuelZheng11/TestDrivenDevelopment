import java.util.*;

public class MockMachineLearningModel implements IMachineLearningModel {
    private String branchName = "";
    private Map<List<Integer>, AnomalyType> anomalies = new HashMap<List<Integer>, AnomalyType>();

    public void loadBranch(IBranch targetBranch) {
        branchName = targetBranch.getName();
    }

    public void runModelOnBranch() {
        if (branchName == "allPassMLCheckBranch") {
            return;
        } else {

            List<Integer> anomolyLineNumbers = Arrays.asList(new Integer[]{1, 2, 3});

            if (branchName == "failSmallDefectsMLCheckBranch") {
                anomalies.put(anomolyLineNumbers, AnomalyType.SmallDefect);
            } else if (branchName == "failMaliciousCodeBlocksMLCheckBranch") {
                anomalies.put(anomolyLineNumbers, AnomalyType.MaliciousCodeBlock);
            } else if (branchName == "failBadCodeSmellsMLCheckBranch") {
                anomalies.put(anomolyLineNumbers, AnomalyType.BadCodeSmell);
            } else {
                anomalies = null;
            }
        }
    }

    public Map<List<Integer>, AnomalyType> getResults() {
        return anomalies;
    }
}
