import java.util.List;

public class GithubModule {
    private static GithubModule _module;

    public static GithubModule getInstance(){
        if (_module == null){
            _module = new GithubModule();
        }
        return _module;
    }

    public User signIn(String username, String password){

    }

    public void signOut(User user){

    }

    public PullRequest createPullRequest(GitBranch branch){

    }

    public PullRequest getPullRequest(String branchName){

    }

    public List<GitCommit> getCommits(String branchName){

    }

    public void approvePullRequest(String branchName, User approver){

    }

    public List<GitDiscussion> getPullRequestDiscussion(String branchName){

    }

    public PullRequest createPullRequest(User user, GitBranch branch) {
    }
}
