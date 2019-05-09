import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AutomationTestSuite {

    @Test
    public void noLintNeededTest() {
        try {
            CommandLineRunner cmd = new CommandLineRunner();
            String runnableCommand = "npm run lint";
            String branch = "noLintNeededBranch";
            cmd.setCommand(runnableCommand);
            ArrayList<String> results = cmd.runLintOnBranch(branch);

            for (result: results) {
                if (result.equals("Linter completed with no linting errors")) {
                    return;
                }
            }

            fail();
        } catch (Excpetion e) {
            fail();
        }
    }

    @Test
    public void lintAutoFixTest() {
        try {
            CommandLineRunner cmd = new CommandLineRunner();
            String runnableCommand = "npm run lint";
            String branch = "lintAutoFixBranch";
            cmd.setCommand(runnableCommand);
            ArrayList<String> results = cmd.runLintOnBranch(branch);

            for (result: results) {
                if (result.equals("Linter completed with no linting errors")) {
                    return;
                }
            }

            fail();
        } catch (Excpetion e) {
            fail();
        }
    }

    @Test
    public void LinterRequiresFurtherAssistanceTest() {
        try {
            CommandLineRunner cmd = new CommandLineRunner();
            String runnableCommand = "npm run lint";
            String branch = "linterRequiresFurtherAssistanceBranch";
            cmd.setCommand(runnableCommand);
            ArrayList<String> results = cmd.run();

            for (result: results) {
                if (result.equals("Linter completed with no linting errors")) {
                    fail();
                }
            }
            return;
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void AutomatedCodeInspectionAllPassTest() {
        try {
            Branch branch = new Branch("allPassMLCheckBranch");
            MachineLearningModelParser mlmp = new MachineLearningModelParser();

            List<List<Integer>> anomalyLineNumbers = mlmp.detectAnomalies(branch);
            if(anomalyLineNumbers.size() != 0) {
                fail();
            }

            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalies(anomalyLineNumbers);
            if (anomalyTypeMap.size() != 0) {
                fail();
            }

            return;
        } catch (Exception e) {
            fail();
        }
        return;
    }

    @Test
    public void AutomatedCodeInspectionFailSmallDefectsTest() {
        try {
            Branch branch = new Branch("failSmallDefectsMLCheckBranch");
            MachineLearningModelParser mlmp = new MachineLearningModelParser();

            List<List<Integer>> anomalyLineNumbers = mlmp.detectAnomalies(branch);
            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalies(anomalyLineNumbers);

            for (lineNumberSet: anomalyTypeMap) {
                if (nomalyTypeMap.get(lineNumberSet) == AnomalyType.SmallDefect) {
                    return;
                }
            }

            fail();
        } catch (Exception e) {
            fail();
        }
        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailMaliciousCodeBlocksTest() {
        try {
            String branch = "failMaliciousCodeBlocksMLCheckBranch";
            MachineLearningModelParser mlmp = new MachineLearningModelParser();

            List<List<Integer>> anomalyLineNumbers = mlmp.detectAnomalies(branch);
            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalies(anomalyLineNumbers);

            for (lineNumberSet: anomalyTypeMap) {
                if (nomalyTypeMap.get(lineNumberSet) == AnomalyType.MaliciousCodeBlock) {
                    return;
                }
            }

            fail();
        } catch (Exception e) {
            fail();
        }
        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailBadCodeSmellsTest() {
        try {
            Branch branch = new Branch("failBadCodeSmellsMLCheckBranch");
            MachineLearningModelParser mlmp = new MachineLearningModelParser();

            List<List<Integer>> anomalyLineNumbers = mlmp.detectAnomalies(branch);
            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalies(anomalyLineNumbers);

            for (lineNumberSet: anomalyTypeMap) {
                if (nomalyTypeMap.get(lineNumberSet) == AnomalyType.BadCodeSmell) {
                    return;
                }
            }

            fail();
        } catch (Exception e) {
            fail();
        }

        fail();
    }


    // need to talk to team about how to get branch
    @Test
    public void TestGenerateCodeAbstractionPassTest() {
        try {
            AbstractionExtension ae = new AbstractionExtension();
            String branch = "passCodeAbstractionGenerationBranch";
            File generatedCodeAbstraction = ae.generateCodeAbstractionFor(branch);

            String fileName = "ExpectedCodeAbstraction.txt";
            String line;
            String expectedCodeAbstractionAsAString = "";
            String actualCodeAbstractionAsAString = "";

            try {
                FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    expectedCodeAbstractionAsAString += line;
                }

                bufferedReader.close();

                fileReader = new FileReader(generatedCodeAbstraction);
                bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    actualCodeAbstractionAsAString += line;
                }

                bufferedReader.close();
            }
            catch(FileNotFoundException ex) {
                fail();
            }
            catch(IOException ex) {
                fail();
            }

            assertEquals(expectedCodeAbstractionAsAString, actualCodeAbstractionAsAString);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void TestGenerateCodeAbstractionUnexpectedLineFailTest() {
        try {
            AbstractionExtension ae = new AbstractionExtension();
            String branch = "failCodeAbstractionGenerationBranch";
            File generatedCodeAbstraction = ae.generateCodeAbstractionFor(branch);

            String fileName = "ExpectedCodeAbstraction.txt";
            String line;
            String expectedCodeAbstractionAsAString = "";
            String actualCodeAbstractionAsAString = "";

            try {
                FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    expectedCodeAbstractionAsAString += line;
                }

                bufferedReader.close();

                fileReader = new FileReader(generatedCodeAbstraction);
                bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    actualCodeAbstractionAsAString += line;
                }

                bufferedReader.close();
            }
            catch(FileNotFoundException ex) {
                fail();
            }
            catch(IOException ex) {
                fail();
            }

            assertEquals(expectedCodeAbstractionAsAString, actualCodeAbstractionAsAString);
        } catch (AssertionError e) {
            return;
        }

        fail();
    }

}
