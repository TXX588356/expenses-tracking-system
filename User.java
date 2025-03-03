
public abstract class User {

	private String username;
	private String password;
	
	
	//return the instance variable (accessor/getter method)
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	//set the instance variable (mutator/setter method)
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	
	//default constructor with no parameter to avoid potential error
	public User() {
	}
	
	//constructor used to initialize both username and password
	public User(String username,String password) {
		this.username = username;
		this.password = password;
	}
	
	public abstract boolean verifyUser(); //will be used to verify the validity of user either for an admin or a client
}
