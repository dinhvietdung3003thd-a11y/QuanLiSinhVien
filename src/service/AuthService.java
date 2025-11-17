package service;

import java.util.*;
import model.user;
import util.FileUtils;
public class AuthService {
	private List<user> users = new ArrayList<>();
	
	public boolean Register(String userName, String passWord, String role){
		for(user u : users) {
			if(u.getUserName().equals(userName)) {
				System.out.println("Tên đăng nhập đã tồn tại !");
				return false;
			}
		}
		users.add(new user(userName, passWord, role));
		System.out.println("đăng kí thành công !");
		return true;
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
	
	public void Check(user currentUser, String oldPass) {
		int attempts = 0;
    	boolean oldPassCorrect = false;

    	// Bước 1: Nhập mật khẩu cũ và kiểm tra
    	while (attempts < 3 && !oldPassCorrect) {
    	    if (currentUser.getPassWord().equals(oldPass)) {
    	        oldPassCorrect = true; 
    	    } else {
    	        attempts++;
    	        System.out.println("Sai mật khẩu! Thử lại (" + attempts + "/3)");
    	    }
    	}

    	// Nếu sau 3 lần vẫn sai → thoát
    	if (!oldPassCorrect) {
    	    System.out.println("Bạn đã nhập sai quá 3 lần. Không thể đổi mật khẩu!");
    	    return;
    	}
	}
	public void changePassword(user currentUser, String oldPass, String newPass) {
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
