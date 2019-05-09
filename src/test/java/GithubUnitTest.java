import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GithubUnitTest {

    private User _developer;
    private GithubModule _github;

    @Before public void initialize(){
        _github = GithubModule.getInstance();
        _developer = new Developer();
        String[] credentials = {"username", "password"};
        _github.signIn(_developer, credentials);
    }

    //Requirement (1)
    @Test
    public void GithubSignInTest() {
        //Given
        User developer = new Developer();
        String[] credentials = {"username", "password"};
        //When
        _github.signIn(developer, credentials);
        //Assert
        assertTrue(developer.isSignedIn());
    }

    @Test
    public void GithubSignOutTest() {
        //Given developer is signed in from initialise

        //When
        _github.signOut(_developer);
        //Assert
        assertFalse(_developer.isSignedIn());
    }


    //Requirement (2)
    @Test
    public void GithubPullRequestFetchTest() {
        //Given
        String branchName = "GithubPullRequestFetchTest_Branch";
        String committed_code = "GithubPullRequestFetchTest_Commit";
        GitBranch branch = new GitBranch(branchName, new GitCommit("Commit Message", committed_code));
        createPullRequest(branch);
        //When
        PullRequest githubPR = _github.getPullRequest(branchName);
        //Assert
        assertTrue(githubPR.source.contains(committed_code));
    }


    //Requirement (3)
    @Test
    public void GithubAutomaticMergeOnApprovalTest() {
        //Given
        String branchName = "GithubPullRequestFetchTest_Branch";
        String committed_code = "GithubPullRequestFetchTest_Commit";
        GitBranch branch = new GitBranch(branchName, new GitCommit("Commit Message", committed_code));
        createPullRequest(branch);
        User approver = new Developer();
        //When
        approvalPullRequest(branchName, approver);
        //Assert
        assertTrue(_github.master.contains(committed_code));
        assertTrue(_github.getPullRequest(branchName).isCompleted());
    }


    //Requirement(4)
    @Test
    public void defaultTestTestMethod1() {
        //Given
        String branchName = "GithubPullRequestFetchTest_Branch";
        String committed_code = "GithubPullRequestFetchTest_Commit";
        GitBranch branch = new GitBranch(branchName, new GitCommit("Commit Message", committed_code));
        Pullrequest pullrequest = createPullRequest(branch);
        User commenter = new NonDeveloper();
        //When
        String comment = "This code is great";
        pullrequest.postComment(commenter, comment);
        String request = "Please make line 34 better";
        pullrequest.postCodeChangeRequest(commenter, request);
        pullrequest.postNonDeveloperFeedback();
        //Assert
        GithubDiscussion discussion = _github.getPullRequestDiscussion(branch);
        assertTrue(discussion.contains(comment));
        assertTrue(discussion.contains(request));
    }
}
