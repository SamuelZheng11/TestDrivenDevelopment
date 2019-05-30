
public class Developers_Side_Tool {

	Network_API networkinterface;
	Abstraction_ForNetwork abstractionextension;

	public String ConnectionEstablished()
	{
		return networkinterface.CreateConnection();
	
	}
	public String Changes_ByTool_Recieved()
	{
		return abstractionextension.fetchAbstraction_fromTool();
	}
	public String Changes_ByReviewer_Recieved(String str)
	{
		return abstractionextension.generateReviewerAbstraction();
	}

}
