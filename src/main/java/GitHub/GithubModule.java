package GitHub;

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
        // TODO: implement later
        return null;
    }

    public void signOut(User user){
        // TODO: implement later
        return;
    }

    public PullRequest createPullRequest(GitBranch branch){
        // TODO: implement later
        return null;
    }

    public PullRequest getPullRequest(String branchName){
        // TODO: implement later
        return null;
    }

    public List<GitCommit> getCommits(String branchName){
        // TODO: implement later
        return null;
    }

    public void approvePullRequest(String branchName, User approver){
        // TODO: implement later
        return;
    }

    public List<GitDiscussion> getPullRequestDiscussion(String branchName){
        // TODO: implement later
        return null;
    }

}
