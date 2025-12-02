package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Thay đổi thông tin phù hợp với máy của bạn
    private static final String URL = "jdbc:mysql://localhost:3306/quanlysinhvien"; 
    private static final String USER = "root"; 
    private static final String PASS = "Dinncairo1@"; // Mật khẩu MySQL của bạn

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đăng ký driver (đối với các bản Java cũ, bản mới có thể bỏ qua dòng này)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("❌ Kết nối thất bại!");
            e.printStackTrace();
        }
        return conn;
    }
}