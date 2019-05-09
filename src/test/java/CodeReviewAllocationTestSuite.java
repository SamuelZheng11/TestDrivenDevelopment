import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeReviewAllocationTestSuite {

    Developer developer;
    @Before
    public void createDeveloper(){
        developer = new Developer();
    }

    //requirement 1:
    /* The developer can add/delete one or more non-developer reviewers in this tool. A database is used to store
     * the reviewers’ information.
     */
    //Testing non-developer should not be able to create a pull request
    @Test(expected = NoAuthorityException)
    public void TestNonDeveloperFailCreate() {
        User user = new User();
        PullRequest pr = user.createPullRequest();
        Assert.fail();
    }

    //Testing a user cannot add a developer to a pr thats not thiers
    @Test(expected = NoAuthorityException)
    public void TestUser() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest();
        user.addCodeReviewer(pr, developer);
    }

    //Testing a user cannot add a developer to a pr thats not thiers
    @Test
    public void TestUser() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest();
        developer.addCodeReviewer(pr, user);
        developer.removeCodeReviewer(pr, user);
    }

    @Test(expected = NoAuthorityException)
    public void TestUser() {
        Developer developer = new Developer();
        User user = new User();
        PullRequest pr = developer.createPullRequest();
        user.removeCodeReviewer(pr, developer);
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
        user.setReviews(10);
        user.setReviews(0);

        PullRequest pr = developer.createPullRequest();
        pr.randomAllocateReviewer();
        List<User> user = pr.getReviwers();
        if(users.length() >0){
            for (User u : users){
                if(u == null){
                    Assert.fail();
                }
            }
        }else{
            Assert.fail();
        }
    }


    /*10)The count will be updated after the allocation. The count information is stored in the database as well.
    * */
    @Test
    public void TestUser() {
        Developer developer = new Developer();

        User user = new User();
        int initialReviewCount = user.getReviewCount();

        PullRequest pr = developer.createPullRequest();
        User user = pr.randomAllocateReviewer();
        Assert.assertEquals(user.getReviewCount(), initialReviewCount+1);

    }

    //make database check?


}