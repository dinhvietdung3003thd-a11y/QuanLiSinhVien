package service;

import model.user; // Giữ nguyên tên class user (chữ thường) như code cũ của bạn
import util.DatabaseConnection;
import java.sql.*;

public class AuthService {

    // Đăng nhập: Kiểm tra trong Database
    public user Login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Tạo đối tượng user từ dữ liệu lấy được
                return new user(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

    // Đăng ký: Thêm người dùng mới vào Database
    public boolean add(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            System.out.println("❌ Lỗi: Tên đăng nhập có thể đã tồn tại!");
            return false;
        }
    }
    
    // Kiểm tra mật khẩu cũ (dùng cho chức năng đổi mật khẩu)
    public boolean check(user currentUser, String oldPass) {
        // Có thể kiểm tra trực tiếp từ object currentUser cho nhanh
        return currentUser.getPassWord().equals(oldPass);
    }
    
    // Đổi mật khẩu: Cập nhật vào Database
    public void changePassword(user currentUser, String newPass) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPass);
            stmt.setString(2, currentUser.getUserName());
            
            stmt.executeUpdate();
            
            // Cập nhật luôn vào object hiện tại để đồng bộ
            currentUser.SetPassWord(newPass);
            System.out.println("✅ Đổi mật khẩu thành công!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}