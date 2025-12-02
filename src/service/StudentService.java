package service;

import model.Student;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    // --- 1. LOGIC LẤY DỮ LIỆU (Thay thế cho getAll cũ) ---
    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Student(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email")
                ));	
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- 2. LOGIC THÊM (addStudent) ---
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, s.getId());
            stmt.setString(2, s.getName());
            stmt.setString(3, s.getEmail());
            
            // executeUpdate trả về số dòng bị ảnh hưởng (>0 là thành công)
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ Lỗi: Mã SV trùng hoặc mất kết nối DB.");
            return false;
        }
    }

    // --- 3. LOGIC SỬA (updateStudent) ---
    public boolean updateStudent(String id, String newName, String newEmail) {
        String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setString(3, id);
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 4. LOGIC XÓA (deleteStudent) ---
    public boolean deleteStudent(String id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            // Lỗi thường gặp: Sinh viên đã có điểm -> Không xóa được do ràng buộc khóa ngoại
            System.out.println("❌ Không thể xóa sinh viên này (có thể do đã có bảng điểm).");
            return false;
        }
    }

    // --- 5. LOGIC TÌM KIẾM (findStudent / findStudentbyName) ---
    
    // Tìm theo ID (Khớp chính xác)
    public Student findStudent(String id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getString("id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm theo Tên (Tìm gần đúng - LIKE)
    public Student findStudentbyName(String name) {
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%"); 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getString("id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tên theo ID (Dùng cho bảng điểm)
    public String getStudentNameById(String id) {
        Student s = findStudent(id);
        return (s != null) ? s.getName() : null;
    }

    // --- 6. LOGIC SẮP XẾP (Thay thế sortByName cũ) ---
    // Thay vì void, hàm này trả về List đã sắp xếp để giao diện Swing hiển thị
    public List<Student> getStudentsSortedByName() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                list.add(new Student(rs.getString("id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}