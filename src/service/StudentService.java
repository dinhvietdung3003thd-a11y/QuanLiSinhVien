package service;

import model.Student;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StudentService {
	// 1. Định nghĩa mẫu Regex chuẩn cho Email
    // Mẫu này yêu cầu: [tên]@[domain].[đuôi] (ví dụ: abc@gmail.com)
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    // 2. Hàm phụ để kiểm tra tính hợp lệ
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    // --- 1. LOGIC LẤY DỮ LIỆU  ---
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

 // --- 2. LOGIC THÊM (Cập nhật: Dùng Exception để báo lỗi cụ thể) ---
    public void addStudent(Student s) throws Exception {
        // 1. Check Email trước
        if (!isValidEmail(s.getEmail())) {
            // Ném ra lỗi cụ thể để GUI bắt được
            throw new Exception("Email không đúng định dạng (ví dụ: abc@gmail.com)!");
        }

        String sql = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, s.getId());
            stmt.setString(2, s.getName());
            stmt.setString(3, s.getEmail());
            
            stmt.executeUpdate(); // Nếu chạy qua dòng này mà không lỗi -> Thành công
            
        } catch (SQLIntegrityConstraintViolationException e) {
            // Lỗi này chắc chắn là trùng khóa chính (ID)
            throw new Exception("Mã sinh viên " + s.getId() + " đã tồn tại!");
        } catch (Exception e) {
            // Các lỗi DB khác (mất mạng, sai tên bảng...)
            e.printStackTrace();
            throw new Exception("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    // --- 3. LOGIC SỬA (updateStudent) ---
 // Sửa kiểu trả về từ boolean -> void để ném Exception
    public void updateStudent(String id, String newName, String newEmail) throws Exception {
        // 1. Validate Email trước khi sửa
        if (!isValidEmail(newEmail)) {
            throw new Exception("Email mới không đúng định dạng!");
        }

        String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setString(3, id);
            
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                // Trường hợp hy hữu: ID không tồn tại hoặc bị xóa lúc đang sửa
                throw new Exception("Không tìm thấy sinh viên có ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    // --- 4. LOGIC XÓA (deleteStudent) ---
    public boolean deleteStudent(String id) {
        // Câu lệnh xóa sinh viên (DB sẽ tự động xóa điểm nhờ ON DELETE CASCADE)
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Đã xóa thành công sinh viên và toàn bộ dữ liệu điểm số liên quan.");
                return true;
            } else {
                System.out.println("⚠️ Không tìm thấy sinh viên có ID: " + id + " để xóa.");
                return false;
            }

        } catch (SQLException e) {
            // Lúc này catch chỉ bắt các lỗi kỹ thuật (mất mạng, sai tên bảng, lỗi SQL server...)
            System.err.println("❌ Lỗi kỹ thuật khi xóa sinh viên: " + e.getMessage());
            e.printStackTrace();
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

 // --- HÀM MỚI: Dành riêng cho nút Tìm Kiếm trên giao diện ---
    public List<Student> searchStudent(String keyword) {
        List<Student> list = new ArrayList<>();
        
        // 1. Dùng OR để tìm cả 2 cột cùng lúc
        // 2. Trả về List (Danh sách) thay vì 1 đối tượng đơn lẻ
        String sql = "SELECT * FROM students WHERE id LIKE ? OR name LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String query = "%" + keyword + "%"; 
            stmt.setString(1, query);
            stmt.setString(2, query);
            
            ResultSet rs = stmt.executeQuery();
            // Dùng vòng lặp while để lấy TẤT CẢ kết quả tìm được
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
    
    // Lấy tên theo ID (Dùng cho bảng điểm)
    public String getStudentNameById(String id) {
        Student s = findStudent(id);
        return (s != null) ? s.getName() : null;
    }

    // --- 6. LOGIC SẮP XẾP (Thay thế sortByName cũ) ---
    // Thay vì void, hàm này trả về List đã sắp xếp để giao diện Swing hiển thị
    public List<Student> getStudentsSortedByName() {
        List<Student> list = new ArrayList<>();
        
        // 1. SUBSTRING_INDEX... : Sắp xếp theo tên (Anh)
        // 2. , name ASC         : Nếu tên trùng, sắp xếp theo cả chuỗi (Hoàng Anh < Phương Anh)
        String sql = "SELECT * FROM students ORDER BY SUBSTRING_INDEX(name, ' ', -1) ASC, name ASC";

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