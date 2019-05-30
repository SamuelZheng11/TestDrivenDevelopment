import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mongodb.MongoClient;

import java.util.List;

public class CodeReviewDatabasePersistenceTest {

    private String sourceBranchName = "GithubPullRequestFetchTest_Branch";
    private String targetBranchName = "GithubPullRequestFetchTest_TargetBranch";

    private User developer = new User("", UserType.Developer);
    private User nonDeveloper = new User("codeReviewer", UserType.NonDeveloper);

    private GitCommit commit = new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit");
    private GitCommit[] committed_code = {commit};
    private GitBranch sourceBranch = new GitBranch(sourceBranchName, committed_code);
    private GitBranch targetBranch = new GitBranch(targetBranchName, committed_code);

    @Test
    public void shouldConnectToMongoDBServer(){
        //Given
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        String usersDBName = "users-db";
        String usersCollectionName = "users-collection";

        GithubApi _github = new MockGithubModule();

        ReviewerPersistence rp = ReviewerPersistence.getInstance();
        rp.addDatabase(mongoClient, usersDBName, usersCollectionName);

        PullRequest mockPullRequest = _github.createPullRequest("Test developer can add code reviewers", sourceBranch, targetBranch);
        int initialReviewCount = nonDeveloper.getReviewCount();

        //When
        CodeReviewAllocation codeReviewAllocation = mockPullRequest.createCodeReview(developer, nonDeveloper);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        assertTrue(codeReviewers.contains(nonDeveloper));

        assertEquals(initialReviewCount+1,nonDeveloper.getReviewCount());

        System.out.println("User Code Review Count save action perfomed!!!");
    }

}
