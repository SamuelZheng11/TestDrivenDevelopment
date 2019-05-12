package Automation;

import GitHub.IBranch;

import java.util.ArrayList;
import java.util.List;

public class CommandLineRunner {

    private List<String> commands = new ArrayList<String>();

    public void setCommand(List<String> commands) {
        for (String command: commands) {
            this.commands.add(command);
        }
    };

    public List<String> runOnBranch(IBranch branch) {return null;}
}
