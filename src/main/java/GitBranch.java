public class GitBranch  implements IBranch {

    private String _name;

    public GitBranch(String name, GitCommit[] commits){
        _name = name;
    }

    public String getName() {
        return _name;
    }

}
