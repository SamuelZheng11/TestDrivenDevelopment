import java.util.List;
import java.util.Map;

public class MachineLearningModelHandler {
    public Map<List<Integer>, AnomalyType> identifyAnomalyLines(IBranch branch) {
        IMachineLearningModel mockMlm = new MockMachineLearningModel();

        mockMlm.loadBranch(branch);
        mockMlm.runModelOnBranch();

        return mockMlm.getResults();
    }
}
