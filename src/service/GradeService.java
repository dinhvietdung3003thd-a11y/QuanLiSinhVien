package service;

import model.Grade;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeService {

    // Thêm hoặc Cập nhật điểm
    // Sử dụng cú pháp đặc biệt của MySQL: ON DUPLICATE KEY UPDATE
    // Nếu chưa có điểm -> Thêm mới. Nếu có rồi -> Cập nhật điểm mới.
    public void addOrUpdateGrade(String sid, String subid, double score) {
        String sql = "INSERT INTO grades (student_id, subject_id, score) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE score = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sid);
            stmt.setString(2, subid);
            stmt.setDouble(3, score);
            stmt.setDouble(4, score); // Tham số thứ 4 là giá trị update
            
            stmt.executeUpdate();
            System.out.println("✅ Đã lưu điểm vào Database!");
            
        } catch (SQLException e) {
            // Lỗi thường gặp: Nhập mã sinh viên hoặc mã môn học không tồn tại
            System.out.println("❌ Lỗi: Mã SV hoặc Mã MH không tồn tại trong hệ thống.");
            e.printStackTrace();
        }
    }

    // Lấy danh sách điểm của 1 sinh viên
    public List<Grade> getGradesByStudent(String sid) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sid);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(new Grade(
                    rs.getString("student_id"),
                    rs.getString("subject_id"),
                    rs.getDouble("score")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tính điểm trung bình (GPA)
    // Tận dụng hàm AVG() của SQL để tính cho nhanh
    public double calculateGPA(String sid) {
        String sql = "SELECT AVG(score) as gpa FROM grades WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double gpa = rs.getDouble("gpa");
                // Nếu sinh viên chưa có điểm nào, gpa sẽ là 0.0 hoặc null -> 0
                return gpa;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}