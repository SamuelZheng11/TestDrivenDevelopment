public class User {
	//IAbstractionExtension used for module 4	
	Abstraction_ForNetwork abstractionextension;
    private final String name;
    public String str ;
    private int reviewCount = 0;
    public User(String name, UserType type){
        this.name = name;
    }

    public User(String userName, UserType nonDeveloper, int reviewCount) {
        this.name = userName;
        this.reviewCount = reviewCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void incrementReviewCount() {
        reviewCount +=1;
        ReviewerPersistence.getInstance().updateReviewCount(this);
    }

    public void decrementReviewCount() {
        reviewCount -=1;
        ReviewerPersistence.getInstance().updateReviewCount(this);
    }

    public String getName(){
        return name;
    }
    //module 4   
    //requirement 11
    //Final comments received from the automated tool
    public String AbstractResult_Recieved_fromTool()
	{
    	return abstractionextension.fetchAbstraction_fromTool();
	}
    //requirement 12
    public String NonDev_AddComment(String str1, String str2) {
    	
    	str = str1+str2;
    	//AbstractResult_Recieved_fromNonDev();
    	return str;
    }
    //requirement 13
    //Final comments received from the Non Developer
    public String AbstractResult_Recieved_fromNonDev()
	{
    	return abstractionextension.generateReviewerAbstraction();
	}
    public String Dev_AddComment(String str1, String str2) {
    	
    	str = str1+str2;
    	//AbstractResult_FinalChange_fromDev();
    	return str;
     }
    //Final comments received from the Developer
    public String AbstractResult_FinalChange_fromDev() {
		return abstractionextension.generateDevCodeAbstraction();

    }
    //Final Developer Approval
    public String verdict;
    public String FinalVeredict(String str,String verdict)
    {
    	if(verdict=="yes")
    		return "approved";
    	else
    		return "disapproved";
    }	
    
}