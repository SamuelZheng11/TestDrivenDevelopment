import java.util.ArrayList;
import java.util.List;

public class MockGithubModule implements GithubApi{

    private GithubUnitTest testvalues;
    private User _currentUser;
    private MockPullRequest mockPR;
    private PullRequest storedPullRequest = null;

    public MockGithubModule(){}

    public MockGithubModule(GithubUnitTest suite){
        testvalues =  suite;
    }

    public User signIn(String username, String password) {
        if (username == testvalues.username && password == testvalues.password){
            _currentUser = testvalues.developer;
            return _currentUser;
        }
        return null;
    }

    public void signOut(User user) {
        _currentUser = null;
    }

    public boolean isSignedIn(User user) {
        return (user.equals(_currentUser));
    }

    public PullRequest createPullRequest(String title, GitBranch head, GitBranch target) {
        mockPR = new MockPullRequest(_currentUser, title, head, target);
        return mockPR;
    }

    public PullRequest getPullRequest(String branchName) {
        return storedPullRequest;
    }

    public List<GitCommit> getCommits(String branchName) {
        ArrayList<GitCommit> list = new ArrayList<GitCommit>();
        list.add(testvalues.commit);
        return list;
    }

    public boolean approvePullRequest(PullRequest pr) {
        storedPullRequest = pr;
        pr.setCompletedStatus(true);
        return true;
    }

    public List<GitComment> getPullRequestComments(PullRequest pullRequest) {
        if (pullRequest == mockPR){
            return mockPR.getCommentsPosted();
        }
        return null;
    }

}
