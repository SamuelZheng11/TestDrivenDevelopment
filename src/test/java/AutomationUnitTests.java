import Automation.*;
import GitHub.GitBranch;
import GitHub.GitCommit;
import GitHub.IBranch;
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
        CommandLineRunner cmd = new CommandLineRunner();
        String runnableCommand = "npm run lint";
        IBranch branch = new GitBranch("noLintNeededBranch", new GitCommit[]{});
        cmd.setCommand(new ArrayList<String>(Arrays.asList(runnableCommand)));
        List<String> results = cmd.runOnBranch(branch);

        for (String result: results) {
            if (result.equals("Linter completed with no linting errors")) {
                return;
            }
        }

        fail();
    }

    @Test
    public void lintAutoFixTest() {
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
    }

    @Test
    public void LinterRequiresFurtherAssistanceTest() {
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
    }

    @Test
    public void AutomatedCodeInspectionAllPassTest() {
        IBranch branch = new GitBranch("allPassMLCheckBranch", new GitCommit[]{});
        MachineLearningModelHandler mlmp = new MachineLearningModelHandler();

        Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

        if (anomalyTypeMap.size() != 0) {
            fail();
        }

        return;
    }

    @Test
    public void AutomatedCodeInspectionFailSmallDefectsTest() {
        IBranch branch = new GitBranch("failSmallDefectsMLCheckBranch", new GitCommit[]{});
        MachineLearningModelHandler mlmp = new MachineLearningModelHandler();

        Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

        for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
            if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.SmallDefect) {
                return;
            }
        }

        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailMaliciousCodeBlocksTest() {
        IBranch branch = new GitBranch("failMaliciousCodeBlocksMLCheckBranch", new GitCommit[]{});
        MachineLearningModelHandler mlmp = new MachineLearningModelHandler();

        Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

        for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
            if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.MaliciousCodeBlock) {
                return;
            }
        }

        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailBadCodeSmellsTest() {
        IBranch branch = new GitBranch("failBadCodeSmellsMLCheckBranch", new GitCommit[]{});
        MachineLearningModelHandler mlmp = new MachineLearningModelHandler();

        Map<List<Integer>, AnomalyType> anomalyTypeMap = mlmp.identifyAnomalyLines(branch);

        for (List<Integer> lineNumberSet: anomalyTypeMap.keySet()) {
            if (anomalyTypeMap.get(lineNumberSet) == AnomalyType.BadCodeSmell) {
                return;
            }
        }

        fail();
    }


    // need to talk to team about how to get branch
    @Test
public void TestGenerateCodeAbstractionPassTest() {
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
    }

    @Test
    public void TestGenerateCodeAbstractionUnexpectedLineFailTest() {
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
    }
}
