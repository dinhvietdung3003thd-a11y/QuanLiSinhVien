package service;

import model.Subject;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectService {

    // Lấy tất cả môn học
    public List<Subject> getAll() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Subject(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("credits")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm môn học
 // 1. Đổi từ void sang boolean
    public boolean addSubject(Subject s) {
        String sqlCheck = "SELECT COUNT(*) FROM subjects WHERE name = ?";
        String sqlInsert = "INSERT INTO subjects (id, name, credits) VALUES (?, ?, ?)";
        
        // Sử dụng 1 kết nối chung cho cả 2 thao tác để tối ưu
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // --- BƯỚC 1: KIỂM TRA TÊN MÔN HỌC ---
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setString(1, s.getName());
                ResultSet rs = checkStmt.executeQuery();
                
                // Nếu đếm được số lượng > 0 nghĩa là tên đã tồn tại
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("❌ Tên môn học '" + s.getName() + "' đã tồn tại!");
                    return false; // Trả về false ngay, không thêm nữa
                }
            }

            // --- BƯỚC 2: THÊM MỚI (Nếu bước 1 qua ải) ---
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setString(1, s.getId());
                stmt.setString(2, s.getName());
                stmt.setInt(3, s.getCredits());
                
                int rows = stmt.executeUpdate();
                return rows > 0; 
            }

        } catch (Exception e) {
            // Lỗi xảy ra (ví dụ trùng Mã ID hoặc lỗi mạng)
            System.out.println("❌ Lỗi thêm môn học: " + e.getMessage());
            return false;
        }
    }

    // Xóa môn học
    public void deleteSubject(String id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            int rows = stmt.executeUpdate();
            if(rows > 0) System.out.println("✅ Đã xóa môn học!");
            else System.out.println("❌ Không tìm thấy môn học để xóa.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tên môn học theo ID (Hỗ trợ hiển thị bảng điểm)
    public String getSubjectNameById(String id) {
        String sql = "SELECT name FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("name");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Không rõ";
    }
    
    // Tìm môn học (để kiểm tra tồn tại)
    public Subject findSubject(String id) {
         String sql = "SELECT * FROM subjects WHERE id = ?";
         try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Subject(rs.getString("id"), rs.getString("name"), rs.getInt("credits"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}