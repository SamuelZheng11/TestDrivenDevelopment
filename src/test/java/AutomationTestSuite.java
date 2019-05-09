import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class AutomationTestSuite {

    @Test
    public void LinterPassTest() {
        CommandLineRunner cmd = new CommandLineRunner();
        String runnableCommand = "npm run lint";
        cmd.setCommand(runnableCommand);
        ArrayList<String> results = cmd.run();

        for (result: results) {
            if (result.equals("Linter completed with no linting errors")) {
                return;
            }
        }

        fail();
    }

    @Test
    public void LinterFailTest() {
        CommandLineRunner cmd = new CommandLineRunner();
        String runnableCommand = "npm run lint";
        cmd.setCommand(runnableCommand);
        ArrayList<String> results = cmd.run();

        for (result: results) {
            if (result.equals("Linter completed with no linting errors")) {
                fail();
            }
        }
    }

    @Test
    public void Test2() {
        
    }

    @Test
    public void Test3() {}

    @Test
    public void Test4() {}

    @Test
    public void Test5() {}

    @Test
    public void Test6() {}

    @Test
    public void Test7() {}

    @Test
    public void Test8() {}

    @Test
    public void Test9() {}

    @Test
    public void Test11() {}

    @Test
    public void Test() {}
}
