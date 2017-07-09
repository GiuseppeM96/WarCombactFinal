package game.manager;

public class User {

	String userName;

	public User(String userName) {
		this.userName = userName;
	}

	public void setName(String text) {
		userName = text;
	}

	public String getName() {
		return userName;
	}

}
