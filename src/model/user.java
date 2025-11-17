package model;

public class user {
	private String userName;
	private String passWord;
	private String role;
	
	public user(String userName, String passWord, String role) {
		this.userName = userName;
		this.passWord = passWord;
		this.role = role;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getPassWord() {
		return passWord;
	}
	
	public String getRole() {
		return role;
	}
	
	public String SetPassWord(String newPassWord) {
		return this.passWord = newPassWord;
	}
}
