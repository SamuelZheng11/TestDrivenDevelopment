import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AutomationUnitTests {

    @Test
    public void noLintNeededTest() {
        try {
            CommandLineRunner cmd = new CommandLineRunner();
            String runnableCommand = "npm run lint";
            IBranch branch = new GitBranch("noLintNeededBranch", new GitCommit[]{});
            cmd.setCommand(new ArrayList<String>(Arrays.asList(runnableCommand)));
            ArrayList<String> results = cmd.runOnBranch(branch);

            for (String result: results) {
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
            IBranch branch = new GitBranch("lintAutoFixBranch", new GitCommit[]{});
            cmd.setCommand(new ArrayList<String>(Arrays.asList(runnableCommand)));
            List<String> results = cmd.runOnBranch(branch);

            for (String result: results) {
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
            IBranch branch = new GitBranch("linterRequiresFurtherAssistanceBranch", new GitCommit[]{});
            cmd.setCommand(new ArrayList<String>(Arrays.asList(runnableCommand)));
            List<String> results = cmd.runOnBranch(branch);

            for (String result: results) {
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
            IBranch branch = new GitBranch("allPassMLCheckBranch", new GitCommit[]{});
            IMachineLearningModel mlmp = new MachineLearningModelHandler();

            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

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

            IBranch branch = new GitBranch("failSmallDefectsMLCheckBranch", new GitCommit[]{});
            IMachineLearningModel mlmp = new MachineLearningModelHandler();

            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

            for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
                if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.SmallDefect) {
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
            IBranch branch = new GitBranch("failMaliciousCodeBlocksMLCheckBranch", new GitCommit[]{});
            IMachineLearningModel mlmp = new MachineLearningModelHandler();

            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

            for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
                if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.MaliciousCodeBlock) {
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
            IBranch branch = new GitBranch("failBadCodeSmellsMLCheckBranch", new GitCommit[]{});
            IMachineLearningModel mlmp = new MachineLearningModelHandler();

            Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

            for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
                if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.BadCodeSmell) {
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
            AbstractionExtension ae = new AbstractionGenerationHandler();
            IBranch branch = new GitBranch("passCodeAbstractionGenerationBranch", new GitCommit[]{});
            File generatedCodeFile = ae.generateCodeAbstractionFor(branch);

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

                fileReader = new FileReader(generatedCodeFile);
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
            AbstractionExtension ae = new AbstractionGenerationHandler();
            IBranch branch = new GitBranch("failCodeAbstractionGenerationBranch", new GitCommit[]{});
            File generatedCodeFile = ae.generateCodeAbstractionFor(branch);

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

                fileReader = new FileReader(generatedCodeFile);
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
