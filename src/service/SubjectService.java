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
    public void addSubject(Subject s) {
        String sql = "INSERT INTO subjects (id, name, credits) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, s.getId());
            stmt.setString(2, s.getName());
            stmt.setInt(3, s.getCredits());
            
            stmt.executeUpdate();
            System.out.println("✅ Thêm môn học thành công!");
        } catch (Exception e) {
            System.out.println("❌ Lỗi: Mã môn học có thể đã tồn tại.");
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