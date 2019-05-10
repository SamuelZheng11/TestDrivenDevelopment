import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GithubUnitTest {

    private User _developer;
    private GithubModule _github;

    @Before public void initialize(){
        _github = GithubModule.getInstance();
        _developer = new User("", UserType.Developer);
        _github.signIn("username", "password");
    }

    //Requirement (1)
    @Test
    public void GithubSignInTest() {
        //Given
        User developer = new User("", UserType.Developer);
        //When
        _github.signIn("username", "password");
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
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);
        _github.createPullRequest(branch);
        //When
        PullRequest githubPR = _github.getPullRequest(branchName);
        //Assert
        assertTrue(_github.getCommits(branchName).contains(committed_code[0]));
    }


    //Requirement (3)
    @Test
    public void GithubAutomaticMergeOnApprovalTest() {
        //Given
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);
        _github.createPullRequest(branch);
        User approver = new User("", UserType.Developer);
        //When
        _github.approvePullRequest(branchName, approver);
        //Assert
        assertTrue(_github.getCommits("master").contains(committed_code));
        assertTrue(_github.getPullRequest(branchName).isCompleted());
    }


    //Requirement(4)
    @Test
    public void AddCommentsAndCodeRequestsToGithubAutomaticallyTest() {
        //Given
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);
        PullRequest pullrequest = _github.createPullRequest(branch);
        User commenter = new User("", UserType.NonDeveloper);
        //When
        GitComment comment = new GitComment("This code is great");
        pullrequest.postDiscussion(commenter, comment);
        GitCodeRequest request = new GitCodeRequest("Please make line 34 better");
        pullrequest.postDiscussion(commenter, request);
        //Assert
        List<GitDiscussion> discussion = _github.getPullRequestDiscussion(branchName);
        assertTrue(discussion.contains(comment));
        assertTrue(discussion.contains(request));
    }
}
