import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.mockito.Mockito;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeReviewAllocationTest {
    private String sourceBranchName = "GithubPullRequestFetchTest_Branch";
    private String targetBranchName = "GithubPullRequestFetchTest_TargetBranch";

    private User developer = new User("", UserType.Developer);
    private User nonDeveloper = new User("codeReviewer", UserType.NonDeveloper);

    private GitCommit commit = new GitCommit("Commit Message", "GithubPullRequestFetchTest_Commit");
    private GitCommit[] committed_code = {commit};
    private GitBranch sourceBranch = new GitBranch(sourceBranchName, committed_code);
    private GitBranch targetBranch = new GitBranch(targetBranchName, committed_code);

    private GithubApi _github;

    private MongoClient mongoClient;
    private String usersDBName;
    private String usersCollectionName;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;
    private ReviewerPersistence spiedRPInstance;

    private FindIterable mockIterable;
    private MongoCursor mockCursor;
    private Document mockCodeReviewerDocument;

    private static String NAME_OF_INSTANCE_FIELD = "instance";
    @Before
    public void initialize(){
        // Given

        _github = new MockGithubModule();
        //mocking database
        mongoClient = mock(MongoClient.class);
        usersDBName = "users-db";
        usersCollectionName = "user-collection";

        mongoDatabase =  mock(MongoDatabase.class);
        Mockito.doReturn(mongoDatabase).when(mongoClient).getDatabase(usersDBName);

        collection = mock(MongoCollection.class);
        Mockito.doReturn(collection).when(mongoDatabase).getCollection(usersCollectionName);

        //spying database behaviour
        spiedRPInstance = Mockito.spy(ReviewerPersistence.getInstance());
        setSpyMock(spiedRPInstance);
        spiedRPInstance.addDatabase(mongoClient, usersDBName, usersCollectionName);

        //mocking database behaviour
        mockIterable = mock(FindIterable.class);
        mockCursor = mock(MongoCursor.class);
        mockCodeReviewerDocument = mock(Document.class);

    }

    private void setSpyMock(ReviewerPersistence mock) {
        try {
            Field instance = ReviewerPersistence.class.getDeclaredField(NAME_OF_INSTANCE_FIELD);
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //To not confuse other tests with a mocked reviewer persistence singleton
    @After
    public void resetSingleton() throws Exception {
        Field instance = ReviewerPersistence.class.getDeclaredField(NAME_OF_INSTANCE_FIELD);
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private void mockDatabaseBehaviourWhenAddReviewCountIsCalled(User user){
        Mockito.when(collection.find(new Document(ReviewerPersistence.FIRST_NAME_KEY, user.getName()))).thenReturn(mockIterable);
        Mockito.when(mockIterable.iterator()).thenReturn(mockCursor);
        Mockito.when(mockCursor.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(mockCursor.next()).thenReturn(mockCodeReviewerDocument);
        Mockito.when(mockCodeReviewerDocument.get(ReviewerPersistence.REVIEW_COUNT_KEY)).thenReturn(user.getReviewCount());
    }

    private void mockDatabaseBehaviourWhenGetAllCodeReviewersIsCalled(){
        Mockito.when(collection.find()).thenReturn(mockIterable);
        Mockito.when(mockIterable.iterator()).thenReturn(mockCursor);
        Mockito.when(mockCursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockCursor.next()).thenReturn(mockCodeReviewerDocument);
        Mockito.when(mockCodeReviewerDocument.get(ReviewerPersistence.REVIEW_COUNT_KEY)).thenReturn(0).thenReturn(5).thenReturn(0);
        Mockito.when(mockCodeReviewerDocument.get(ReviewerPersistence.USERTYPE_KEY)).thenReturn(UserType.NonDeveloper).thenReturn(UserType.NonDeveloper).thenReturn(UserType.Developer);
        Mockito.when(mockCodeReviewerDocument.get(ReviewerPersistence.FIRST_NAME_KEY)).thenReturn("1").thenReturn("2").thenReturn("3");
    }

    /**
     * 8. The developer can add/delete one or more non-developer reviewers in this
     * tool. A database is used to store the reviewers’ information.
     */
    @Test
    public void shouldAllowDeveloperToAddCodeReviewer() {
        //Given
        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test developer can add code reviewers", sourceBranch, targetBranch));
        int initialReviewCount = nonDeveloper.getReviewCount();

        //When
        CodeReviewAllocation codeReviewAllocation = mockPullRequest.createCodeReview(developer, nonDeveloper);
        mockDatabaseBehaviourWhenAddReviewCountIsCalled(nonDeveloper);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        assertTrue(codeReviewers.contains(nonDeveloper));

        assertEquals(initialReviewCount+1,nonDeveloper.getReviewCount());

        Mockito.verify(spiedRPInstance, times(1)).updateReviewCount(nonDeveloper);
        Mockito.verify(mockPullRequest, times(1)).createCodeReview(developer, nonDeveloper);
    }

    @Test
    public void shouldAllowDeveloperToRemoveCodeReviewer() {

        //Given
        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test developer can remove code reviewers", sourceBranch, targetBranch));
        int initialReviewCount = nonDeveloper.getReviewCount();

        CodeReviewAllocation codeReviewAllocation = mockPullRequest.createCodeReview(developer, nonDeveloper);
        mockDatabaseBehaviourWhenAddReviewCountIsCalled(nonDeveloper);

        //check that the database is initially correct and the count has actually been increased
        int databaseReviewCount = spiedRPInstance.getReviewCountForUser(nonDeveloper);
        assertEquals(nonDeveloper.getReviewCount(), databaseReviewCount);
        assertEquals(initialReviewCount+1,nonDeveloper.getReviewCount());

        //When
        mockPullRequest.removeCodeReviwer(developer, nonDeveloper);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        assertFalse(codeReviewers.contains(nonDeveloper));
        assertEquals(initialReviewCount,nonDeveloper.getReviewCount());
        Mockito.verify(spiedRPInstance, times(2)).updateReviewCount(nonDeveloper);
        Mockito.verify(mockPullRequest, times(1)).createCodeReview(developer, nonDeveloper);
        Mockito.verify(mockPullRequest, times(1)).removeCodeReviwer(developer, nonDeveloper);

    }


    /**
     * 9) The tool can randomly choose a reviewer and allocate code review task to
     * this reviewer. The chance of being allocated is related to the count of
     * reviews previously done by a reviewer, the lower count, the higher chance.
     */
    @Test
    public void shouldAllowToolToRandomlyAllocateCodeReviewerToPullRequest() {

        //Given
        mockDatabaseBehaviourWhenGetAllCodeReviewersIsCalled();
        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test random code reviewers", sourceBranch, targetBranch));

        //When
        CodeReviewAllocation codeReviewAllocation = new CodeReviewAllocation(mockPullRequest);
        User randomlyAllocatedReviewer = codeReviewAllocation.randomAllocateReviewer();

        mockDatabaseBehaviourWhenAddReviewCountIsCalled(randomlyAllocatedReviewer);

        //check that the database is initially correct
        int databaseReviewCount = spiedRPInstance.getReviewCountForUser(randomlyAllocatedReviewer);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        if(codeReviewers.size() != 1){
            fail("Random allocation did not allocate ONE code reviewer");
        }
        assertNotNull(codeReviewers.get(0));
        Mockito.verify(spiedRPInstance, times(1)).updateReviewCount(randomlyAllocatedReviewer);
        Mockito.verify(mockPullRequest, times(1)).addCodeReview(codeReviewAllocation);

    }

    /** 10)The count will be updated after the allocation. The count information is
     * stored in the database as well.
     */
    @Test
    public void shouldAutomaticallyIncrementRandomlyAllocatedCodeReviewerCount() {
        //Given
        mockDatabaseBehaviourWhenGetAllCodeReviewersIsCalled();

        List<User> allUsers = spiedRPInstance.getAllCodeReviewers();
        Map<User, Integer> userReviewCountMap = new HashMap<User, Integer>();
        //getting all review counts of all users
        for(User u : allUsers){
            userReviewCountMap.put(u, u.getReviewCount());
        }

        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test random code reviewers count", sourceBranch, targetBranch));

        //When
        CodeReviewAllocation codeReviewAllocation = new CodeReviewAllocation(mockPullRequest);
        codeReviewAllocation.randomAllocateReviewer();

        //Then
        List<User> users = mockPullRequest.getCodeReviewers();
        User codeReviewer = users.get(0);
        int initialReviewCount = userReviewCountMap.get(codeReviewer);
        assertEquals(codeReviewer.getReviewCount(), initialReviewCount+1);

        Mockito.verify(spiedRPInstance, times(1)).updateReviewCount(codeReviewer);
        Mockito.verify(mockPullRequest, times(1)).addCodeReview(codeReviewAllocation);
    }

    /**
     * 8. The developer can add/delete one or more non-developer reviewers in this
     * tool. A database is used to store the reviewers’ information.
     */
    @Test
    public void shouldAllowDeveloperToAddMultipleCodeReviewers() {
        //Given
        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test developer can add multiple code reviewers", sourceBranch, targetBranch));

        mockDatabaseBehaviourWhenGetAllCodeReviewersIsCalled();
        List<User> allUsers = spiedRPInstance.getAllCodeReviewers();
        Map<User, Integer> userReviewCountMap = new HashMap<User, Integer>();
        //getting all review counts of all users
        for(User u : allUsers){
            userReviewCountMap.put(u, u.getReviewCount());
            mockDatabaseBehaviourWhenAddReviewCountIsCalled(u);
        }

        List<User> allCodeReviewers = spiedRPInstance.getAllCodeReviewers();

        //When
        List<CodeReviewAllocation> codeReviewAllocations = mockPullRequest.createCodeReview(developer, allCodeReviewers);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        assertTrue(codeReviewers.containsAll(allCodeReviewers));
        for(User u: userReviewCountMap.keySet()) {
            assertEquals(userReviewCountMap.get(u) + 1, u.getReviewCount());
        }

        for(User u : allUsers){
            Mockito.verify(spiedRPInstance, times(1)).updateReviewCount(u);
            Mockito.verify(mockPullRequest, times(1)).createCodeReview(developer, u);
        }
    }

    @Test
    public void shouldAllowDeveloperToRemoveMultipleCodeReviewers() {
        //Given
        PullRequest mockPullRequest = Mockito.spy(_github.createPullRequest("Test developer can remove multiple code reviewers", sourceBranch, targetBranch));

        mockDatabaseBehaviourWhenGetAllCodeReviewersIsCalled();
        List<User> allUsers = spiedRPInstance.getAllCodeReviewers();
        Map<User, Integer> userReviewCountMap = new HashMap<User, Integer>();
        //getting all review counts of all users
        for(User u : allUsers){
            userReviewCountMap.put(u, u.getReviewCount());
            mockDatabaseBehaviourWhenAddReviewCountIsCalled(u);
        }

        List<User> allCodeReviewers = spiedRPInstance.getAllCodeReviewers();

        //When
        List<CodeReviewAllocation> codeReviewAllocations = mockPullRequest.createCodeReview(developer, allCodeReviewers);

        //Then
        List<User> codeReviewers = mockPullRequest.getCodeReviewers();
        assertTrue(codeReviewers.containsAll(allCodeReviewers));
        for(User u: userReviewCountMap.keySet()) {
            assertEquals(userReviewCountMap.get(u) + 1, u.getReviewCount());
        }

        mockPullRequest.removeCodeReviwer(developer, allCodeReviewers);
        List<User> prCodeReviewers = mockPullRequest.getCodeReviewers();
        for(User u : allUsers){
            assertFalse(prCodeReviewers.contains(u));
            assertEquals((int)userReviewCountMap.get(u),u.getReviewCount());
            Mockito.verify(spiedRPInstance, times(2)).updateReviewCount(u);
            Mockito.verify(mockPullRequest, times(1)).createCodeReview(developer, u);
            Mockito.verify(mockPullRequest, times(1)).removeCodeReviwer(developer, u);
        }
    }
}
