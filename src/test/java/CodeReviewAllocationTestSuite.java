import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeReviewAllocationTestSuite {

    Developer developer;
    @Before
    public void createDeveloper(){
        developer = new Developer();
    }

    /* The developer can add/delete one or more non-developer reviewers in this tool. A database is used to store
     * the reviewers’ information.
     */
    //Testing non-developer should not be able to create a pull request
    @Test(expected = NoAuthorityException)
    public void TestNonDeveloperFailCreate() {
        User user = new User();
        PullRequest pr = user.createPullRequest(branch);
        Assert.fail();
    }

    //Testing a user cannot add a developer to a pr thats not thiers
    @Test(expected = NoAuthorityException)
    public void TestNonPRCreaterTryToAddCodeReviewer() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest(branch);
        user.addCodeReviewer(pr, developer);
        Assert.fail();
    }

    //Testing developer add/remove a code reviewer
    @Test
    public void TestDeveloperCanAddCodeReviewer() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest(branch);
        developer.addCodeReviewer(pr, user);
    }

    //Testing developer add/remove a code reviewer
    @Test
    public void TestDeveloperCanAddCodeReviewer() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest(branch);
        developer.addCodeReviewer(pr, user);
        developer.removeCodeReviewer(pr, user);
    }

    //Testing a user cannot remove code reviewer from a pr thats not thiers
    @Test(expected = NoAuthorityException)
    public void TestUser() {
        Developer developer = new Developer();
        User user = new User();
        User user1 = new User();
        PullRequest pr = developer.createPullRequest(branch);
        developer.addCodeReviewer(pr, user1);
        user.removeCodeReviewer(pr, user1);
    }


    /*
    * 9) The tool can randomly choose a reviewer and allocate code review task to this reviewer. The chance of being
    * allocated is related to the count of reviews previously done by a reviewer, the lower count, the higher chance.
    */

    //how to check randomness?
    @Test
    public void TestUser() {
        Developer developer = new Developer();

        User user = new User();

        PullRequest pr = developer.createPullRequest(branch);
        pr.randomAllocateReviewer();
        ArrayList<User> users = pr.getReviwers();
        if(users.length() >0){
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
        Developer developer = new Developer();

        User user = new User(); //How to get user before random allocation???????????????????????
        int initialReviewCount = user.getReviewCount();

        PullRequest pr = developer.createPullRequest(branch);
        User user = pr.randomAllocateReviewer();
        Assert.assertEquals(user.getReviewCount(), initialReviewCount+1);

    }

}