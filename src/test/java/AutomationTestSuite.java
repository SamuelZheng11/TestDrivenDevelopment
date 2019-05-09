import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

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
            String branch = "allPassMLCheckBranch";
            MachineLearningModelParser.checkForSmallDefects(branch);
            MachineLearningModelParser.checkForBadCodeSmells(branch);
            MachineLearningModelParser.checkForSuspiciousCodeBlocks(branch);
        } catch (MachineLearningModelException mlme) {
            fail();
        }
        return;
    }

    @Test
    public void AutomatedCodeInspectionFailSmallDefectsTest() {
        try {
            String branch = "failSmallDefectsMLCheckBranch";
            MachineLearningModelParser.checkForBadCodeSmells(branch);
            MachineLearningModelParser.checkForSuspiciousCodeBlocks(branch);
            MachineLearningModelParser.checkForSmallDefects(branch);
        } catch (MachineLearningModelException mlme) {
            if (mlme instanceof SmallDefectDetectedException) {
                return;
            } else {
                fail();
            }
        }
        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailSuspiciousCodeBlocksTest() {
        try {
            String branch = "failSuspiciousCodeBlocksMLCheckBranch";
            MachineLearningModelParser.checkForBadCodeSmells(branch);
            MachineLearningModelParser.checkForSmallDefects(branch);
            MachineLearningModelParser.checkForSuspiciousCodeBlocks(branch);
        } catch (MachineLearningModelException mlme) {
            if (mlme instanceof SuspiciousCodeBlocksDetectedException) {
                return;
            } else {
                fail();
            }
        }
        fail();
    }

    @Test
    public void AutomatedCodeInspectionFailBadCodeSmellsTest() {
        try {
            String branch = "failBadCodeSmellsMLCheckBranch";
            MachineLearningModelParser.checkForSuspiciousCodeBlocks(branch);
            MachineLearningModelParser.checkForSmallDefects(branch);
            MachineLearningModelParser.checkForBadCodeSmells(branch);
        } catch (MachineLearningModelException mlme) {
            if (mlme instanceof BadCodeSmellsDetectedException) {
                return;
            } else {
                fail();
            }
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
