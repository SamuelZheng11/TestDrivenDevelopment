
public interface Network_API {
	//requirement 11
		String CreateConnection();
	//requirement 12
		String Auto_ReviewSent(User nonDeveloper, String str1);
		String Auto_ReviewRecieved();
	//requirement 13
		String NonDev_ReviewSent(User developer, String str2);
		String NonDev_ReviewRecieved();

}
