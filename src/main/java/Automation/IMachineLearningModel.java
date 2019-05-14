package Automation;

import GitHub.IBranch;

import java.util.List;
import java.util.Map;

public interface IMachineLearningModel {

    Map<List<Integer>, AnomalyType> identifyAnomalyLines(IBranch branch);
}
