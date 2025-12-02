package view;

import model.*;
import service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminMainView extends JFrame {
    private StudentService studentService = new StudentService();
    private SubjectService subjectService = new SubjectService();
    private GradeService gradeService = new GradeService();
    private user currentUser;

    public AdminMainView(user u) {
        this.currentUser = u;
        setTitle("Hệ Thống Quản Lý (Admin: " + u.getUserName() + ")");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Tab để chuyển đổi giữa các màn hình
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản Lý Sinh Viên", createStudentPanel());
        tabbedPane.addTab("Quản Lý Môn Học", createSubjectPanel());
        tabbedPane.addTab("Quản Lý Điểm", createGradePanel());

        add(tabbedPane, BorderLayout.CENTER);
        
        // Nút Đăng xuất ở dưới cùng
        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- TAB 1: GIAO DIỆN SINH VIÊN ---
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        
        inputPanel.add(new JLabel("Mã SV:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("Họ Tên:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(txtEmail);
        
        panel.add(inputPanel, BorderLayout.NORTH);

        // Bảng danh sách
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Email"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Các nút chức năng
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDel = new JButton("Xóa");
        JButton btnSort = new JButton("Sắp xếp tên");
        JButton btnLoad = new JButton("Làm mới");
        
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDel); 
        btnPanel.add(btnSort); btnPanel.add(btnLoad);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // --- Logic ---
        // Hàm load dữ liệu lên bảng
        Runnable loadData = () -> {
            model.setRowCount(0);
            for (Student s : studentService.getAll()) 
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
        };
        
        // Sự kiện click bảng để điền ngược lên ô nhập liệu
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtEmail.setText(model.getValueAt(row, 2).toString());
            }
        });

        btnAdd.addActionListener(e -> {
            if(studentService.addStudent(new Student(txtId.getText(), txtName.getText(), txtEmail.getText()))) {
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                loadData.run();
                txtId.setText(""); txtName.setText(""); txtEmail.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thất bại! Mã SV trùng hoặc lỗi mạng.");
            }
        });
        
        btnUpdate.addActionListener(e -> {
            if(studentService.updateStudent(txtId.getText(), txtName.getText(), txtEmail.getText())) {
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                loadData.run();
            } else { JOptionPane.showMessageDialog(this, "❌ Lỗi cập nhật!"); }
        });
        
        btnDel.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?") == 0) {
                if(studentService.deleteStudent(txtId.getText())) {
                    JOptionPane.showMessageDialog(this, "✅ Đã xóa!");
                    loadData.run();
                } else { JOptionPane.showMessageDialog(this, "❌ Không xóa được (Có thể SV đang có điểm)."); }
            }
        });
        
        btnSort.addActionListener(e -> {
            model.setRowCount(0);
            for (Student s : studentService.getStudentsSortedByName()) 
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
        });
        
        btnLoad.addActionListener(e -> loadData.run());
        
        loadData.run(); // Load lần đầu khi mở app
        return panel;
    }

    // --- TAB 2: GIAO DIỆN MÔN HỌC ---
    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));
        
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCredit = new JTextField();
        
        inputPanel.add(new JLabel("Mã MH:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("Tên MH:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Tín chỉ:")); inputPanel.add(txtCredit);
        panel.add(inputPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã MH", "Tên MH", "Tín chỉ"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnDel = new JButton("Xóa");
        btnPanel.add(btnAdd); btnPanel.add(btnDel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        Runnable loadData = () -> {
            model.setRowCount(0);
            for (Subject s : subjectService.getAll()) 
                model.addRow(new Object[]{s.getId(), s.getName(), s.getCredits()});
        };

        btnAdd.addActionListener(e -> {
            try {
                int credit = Integer.parseInt(txtCredit.getText());
                subjectService.addSubject(new Subject(txtId.getText(), txtName.getText(), credit));
                loadData.run();
                JOptionPane.showMessageDialog(this, "✅ Đã thêm!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "❌ Tín chỉ phải là số!"); }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                subjectService.deleteSubject(model.getValueAt(row, 0).toString());
                loadData.run();
                JOptionPane.showMessageDialog(this, "✅ Đã xóa!");
            }
        });

        loadData.run();
        return panel;
    }

    // --- TAB 3: GIAO DIỆN ĐIỂM SỐ ---
    private JPanel createGradePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập điểm"));
        
        JTextField txtSid = new JTextField();
        JTextField txtSubId = new JTextField();
        JTextField txtScore = new JTextField();
        
        inputPanel.add(new JLabel("Mã SV:")); inputPanel.add(txtSid);
        inputPanel.add(new JLabel("Mã MH:")); inputPanel.add(txtSubId);
        inputPanel.add(new JLabel("Điểm số:")); inputPanel.add(txtScore);
        
        JButton btnSave = new JButton("Lưu Điểm");
        inputPanel.add(new JLabel("")); inputPanel.add(btnSave); // Dòng trống để căn chỉnh
        
        panel.add(inputPanel, BorderLayout.NORTH);

        // Bảng xem điểm
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã SV", "Mã MH", "Điểm"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Tìm kiếm
        JPanel searchPanel = new JPanel();
        JTextField txtSearch = new JTextField(10);
        JButton btnSearch = new JButton("Xem bảng điểm SV này");
        JLabel lblGpa = new JLabel("  GPA: ...");
        
        searchPanel.add(new JLabel("Nhập mã SV:")); 
        searchPanel.add(txtSearch); 
        searchPanel.add(btnSearch);
        searchPanel.add(lblGpa);
        
        panel.add(searchPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            try {
                double score = Double.parseDouble(txtScore.getText());
                if(score < 0 || score > 10) {
                    JOptionPane.showMessageDialog(this, "❌ Điểm phải từ 0 - 10!");
                    return;
                }
                gradeService.addOrUpdateGrade(txtSid.getText(), txtSubId.getText(), score);
                JOptionPane.showMessageDialog(this, "✅ Đã lưu điểm!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "❌ Điểm không hợp lệ hoặc sai Mã!"); }
        });

        btnSearch.addActionListener(e -> {
            String sid = txtSearch.getText();
            model.setRowCount(0);
            List<Grade> list = gradeService.getGradesByStudent(sid);
            if(list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy điểm của SV này!");
            } else {
                for(Grade g : list) model.addRow(new Object[]{g.getStudentId(), g.getSubjectId(), g.getScore()});
                double gpa = gradeService.calculateGPA(sid);
                lblGpa.setText("  GPA: " + String.format("%.2f", gpa));
                lblGpa.setForeground(Color.RED);
            }
        });

        return panel;
    }
}