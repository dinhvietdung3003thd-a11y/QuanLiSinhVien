package view;

import model.Grade;
import model.user;
import service.GradeService;
import service.StudentService;
import service.SubjectService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPortalView extends JFrame {
	/**
	 * khai báo phiên bản
	 */
	private static final long serialVersionUID = 1L;
	private StudentService studentService = new StudentService();
    private GradeService gradeService = new GradeService();
    private SubjectService subjectService = new SubjectService();
    private user currentUser;

    public StudentPortalView(user u) {
        this.currentUser = u;
        setTitle("Cổng Thông Tin Sinh Viên");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        String realName = studentService.getStudentNameById(u.getUserName());
        // Nếu không tìm thấy tên (ví dụ admin mới tạo user nhưng chưa tạo hồ sơ SV)
        if (realName == null) {
            realName = u.getUserName();
        }
        JLabel lblHello = new JLabel("Xin chào sinh viên : " + realName, SwingConstants.CENTER);
        lblHello.setFont(new Font("Arial", Font.BOLD, 18));
        lblHello.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(lblHello, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã Môn", "Tên Môn Học", "Điểm Số"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load data logic
        // Lưu ý: Username của Student cũng chính là Mã Sinh Viên (ID)
        String studentId = u.getUserName();
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        
        for (Grade g : grades) {
            String subName = subjectService.getSubjectNameById(g.getSubjectId());
            model.addRow(new Object[]{g.getSubjectId(), subName, g.getScore()});
        }

        // Footer showing GPA
        double gpa = gradeService.calculateGPA(studentId);
        
        JPanel footer = new JPanel();
        JLabel lblGpa = new JLabel("GPA Tích Lũy: " + String.format("%.2f", gpa));
        lblGpa.setFont(new Font("Arial", Font.BOLD, 16));
        lblGpa.setForeground(Color.RED);
        
        // nút đăng xuất
        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        
        //nút đổi mật khẩu
        JButton btnChangePass = new JButton("Đổi Mật Khẩu");
        btnChangePass.addActionListener(e -> {
            new ChangePasswordView(currentUser).setVisible(true);
        });

        footer.add(lblGpa);
        footer.add(Box.createHorizontalStrut(50)); // Khoảng cách
        footer.add(btnChangePass);
        footer.add(btnLogout);
        add(footer, BorderLayout.SOUTH);
    }
}