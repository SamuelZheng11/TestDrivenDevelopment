import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CommandLineRunner {

    private String commands = "";
    private List<String> cliOutput = new ArrayList<String>();

    public void setCommand(String command) {
        commands += command;
    }

    public List<String> runOnBranch(IBranch branch) {
        commands = "git checkout " + branch.getName() + commands;
        MockCommandLineInterface cli = new MockCommandLineInterface();
        cli.exec(commands);

        IBufferedReader reader = cli.getInputStream();
        
        String output;
        while((output = reader.readLine()) != null && output != "") {
            cliOutput.add(output);
        }

        return cliOutput;
    }
}
