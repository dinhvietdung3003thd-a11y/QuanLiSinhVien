package service;

import java.util.*;
import model.user;
import util.FileUtils;
public class AuthService {
	private List<user> users = new ArrayList<>();
	
	public boolean CheckName(String userName){
		for(user u : users) {
			if(u.getUserName().equals(userName)) {
				System.out.println("Tên đăng nhập đã tồn tại !");
				return false;
			}
		}
		return true;
	}
	
	public void add(String userName, String passWord, String role) {
		users.add(new user(userName, passWord, role));
		System.out.println("đăng kí thành công !");
	}
	
	public user Login(String userName, String passWord) {
		for(user u : users) {
			if(u.getUserName().equals(userName) && u.getPassWord().equals(passWord)) {
				return u;
			}
		}
		System.out.println("Tên đăng nhập hoặc tài khoản không tồn tại !");	
		return null;
	}
	

	public boolean check(user currentUser, String oldPass) {
	    if (currentUser.getPassWord().equals(oldPass)) {
	        return true; 
	    } else {
	        return false;
	    }
	}
	public void changePassword(user currentUser, String newPass) { 
	    currentUser.SetPassWord(newPass); 
	    System.out.println("Đổi mật khẩu thành công!");
	}
	 
	public void saveToFile() {
	        List<String> lines = new ArrayList<>();
	        for (user u : users)
	            lines.add(u.getUserName() + "," + u.getPassWord() + "," + u.getRole());
	        FileUtils.writeFile("users.csv", lines);
	    }

	public void loadFromFile() {
	        List<String> lines = FileUtils.readFile("users.csv");
	        for (String line : lines) {
	            String[] p = line.split(",");
	            if (p.length >= 3)
	                users.add(new user(p[0], p[1], p[2]));
	        }
	    }
}
