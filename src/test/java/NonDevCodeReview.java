import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AutomationTestSuite {
	
	
	private GithubModule _github;

    @Before public void initialize(){
        _github = GithubModule.getInstance();
        _github.signIn("username", "password");
}
	
	
	//Requirement 11 Given initial review and abstraction, when reviewer is chosen , then send it to reviewer

	@Test 
	public void TestSendAutomatedCodeReviewAndAbstraction() {
		//Given
		String branch = "sendAutomatedCodeReviewAndAbstraction";
		PullRequest pullRequest = _github.createPullRequest(branch);
		NonDeveloperCodeReview nonDeveloperCodeReview = new NonDeveloperCodeReview();
		
		//when
		File actualResult = nonDeveloperCodeReview.sendAutomatedCodeReviewAndAbstraction(branch, pullRequest);
		
		//then
		assertNotNull(actualResult);
	}
	
	@Test 
	public void TestGetAutomatedCodeReviewAndAbstraction() {
		//Given
		String branch = "sendAutomatedCodeReviewAndAbstraction";
		PullRequest pullRequest = _github.createPullRequest(branch);
		NonDeveloperCodeReview nonDeveloperCodeReview = new NonDeveloperCodeReview();
		
		//when
		File actualResult = nonDeveloperCodeReview.getAutomatedCodeReviewAndAbstraction(branch, pullRequest);
		
		//then
		assertNotNull(actualResult);
	}
	
	//Requirement 12 non developer performs high level code review- reviewer gets a notification that she has a new code to review
	
	//Requirement 13 
	//reviewer can return comment and code change requests as feedback
	
	@Test 
	public void TestPostDiscussion() {
		//Given
		String commenter = "Reviewer1";
		String comment = "Looks good";
		NonDeveloperCodeReview nonDeveloperCodeReview = new NonDeveloperCodeReview();
		
		
		//when
		boolean actualResult = nonDeveloperCodeReview.postDiscussion(commenter, comment);
		
		//then
		assertEquals(Boolean.TRUE, actualResult);
	}
	
	@Test 
	public void TestGetDiscussion() {
		//Given
		String branch = "sendAutomatedCodeReviewAndAbstraction";
		PullRequest pullRequest = _github.createPullRequest(branch);
		String comment = "Looks good";
		NonDeveloperCodeReview nonDeveloperCodeReview = new NonDeveloperCodeReview();
		
		
		//when
		List<GitDiscussion> actualResult = nonDeveloperCodeReview.getDiscussion(branch, pullRequest);
		
		//then
		assertTrue(actualResult.contains(comment));
	}
	
	@Test 
	public void TestApproveReview() {
		//Given
		String branch = "sendAutomatedCodeReviewAndAbstraction";
		PullRequest pullRequest = _github.createPullRequest(branch);
		NonDeveloperCodeReview nonDeveloperCodeReview = new NonDeveloperCodeReview();
		
		//when
		boolean actualResult = nonDeveloperCodeReview.approveReview(branch, pullRequest);
		
		//then
		assertEquals(Boolean.TRUE, actualResult);
	}
	
}
