import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class CodeReviewAllocationTestSuite {
    private GithubModule _github;

    @Before
    public void initialize(){
        _github = GithubModule.getInstance();
    }

    /* The developer can add/delete one or more non-developer reviewers in this tool. A database is used to store
     * the reviewers’ information.
     */
    //Testing non-developer should not be able to create a pull request
    @Test(expected = NoAuthorityException.class)
    public void TestNonDeveloperFailCreate() {
        User user = new User("", UserType.NonDeveloper);
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);
        _github.createPullRequest(user, branch);
        Assert.fail();
    }

    //Testing a user cannot add a developer to a pr thats not thiers
    @Test(expected = NoAuthorityException.class)
    public void TestNonPRCreaterTryToAddCodeReviewer() {
        User developer = new User("", UserType.Developer);
        User user = new User("", UserType.NonDeveloper);
        User cr = new User("", UserType.NonDeveloper);
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pullRequest = _github.createPullRequest(developer, branch);
        pullRequest.addCodeReviwer(user, cr);
        Assert.fail();
    }

    @Test
    public void TestDeveloperCanAddCodeReviewer() {
        User developer = new User("", UserType.Developer);
        User user = new User("", UserType.NonDeveloper);
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pullRequest = _github.createPullRequest(developer, branch);
        pullRequest.addCodeReviwer(developer, user);

    }

    @Test
    public void TestDeveloperCanRemoveCodeReviewer() {
        User developer = new User("", UserType.Developer);
        User user = new User("", UserType.NonDeveloper);
        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pullRequest = _github.createPullRequest(developer, branch);
        pullRequest.addCodeReviwer(developer, user);
        pullRequest.removeCodeReviwer(developer, user);
    }

    //Testing a user cannot remove code reviewer from a pr thats not thiers
    @Test(expected = NoAuthorityException.class)
    public void TestNonPRCreatorCantRemoveCR() {

        User developer = new User("", UserType.Developer);
        User user = new User("", UserType.NonDeveloper);
        User cr = new User("", UserType.NonDeveloper);

        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pullRequest = _github.createPullRequest(developer, branch);
        pullRequest.addCodeReviwer(developer, cr);
        pullRequest.removeCodeReviwer(user, cr);

    }


    /*
    * 9) The tool can randomly choose a reviewer and allocate code review task to this reviewer. The chance of being
    * allocated is related to the count of reviews previously done by a reviewer, the lower count, the higher chance.
    */

    //how to check randomness?
    @Test
    public void TestRandomAllocateCRerToPR() {
        User developer = new User("", UserType.Developer);

        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pr = _github.createPullRequest(developer, branch);

        pr.randomAllocateReviewer();
        ArrayList<User> users = pr.getCodeReviwers();
        if(users.size() > 0){
            for (User u : users){
                if(u == null){
                    Assert.fail("There are no code reviewers");
                }
            }
        }else{
            Assert.fail("No code reviewer was randomly allocated");
        }
    }


    /*10)The count will be updated after the allocation. The count information is stored in the database as well.
    * */
    @Test
    public void TestUser() {
        User developer = new User("", UserType.Developer);

        ArrayList<User> allUsers = Database.getInstance().getAllUsers();
        HashMap<User, Integer> userReviewCountMap = new HashMap<User, Integer>();
        //getting all review counts of all users
        for( User u:allUsers){
            userReviewCountMap.put(u, u.getReviewCount());
        }

        String branchName = "GithubPullRequestFetchTest_Branch";
        GitCommit[] committed_code = {new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit")};
        GitBranch branch = new GitBranch(branchName, committed_code);

        PullRequest pr = _github.createPullRequest(developer, branch);

        pr.randomAllocateReviewer();
        ArrayList<User> users = pr.getCodeReviwers();
        User codeReviewer = users.get(0);
        int initialReviewCount = userReviewCountMap.get(codeReviewer);
        Assert.assertEquals(codeReviewer.getReviewCount(), initialReviewCount+1);

    }

}