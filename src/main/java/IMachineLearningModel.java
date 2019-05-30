import java.util.List;
import java.util.Map;

public interface IMachineLearningModel {
    void loadBranch(IBranch branch);

    void runModelOnBranch();

    Map<List<Integer>, AnomalyType> getResults();
}
