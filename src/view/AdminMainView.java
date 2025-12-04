package view;

import model.*;
import service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminMainView extends JFrame {
    /**
	 * số định dạng để đọc đúng phiên bản
	 */
	private static final long serialVersionUID = 1L;
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
        
        // Nút đổi mật khẩu 
        JButton btnChangePass = new JButton("Đổi Mật Khẩu");
        btnChangePass.addActionListener(e -> {
            new ChangePasswordView(currentUser).setVisible(true);
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnChangePass);
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);
    }

 // --- TAB 1: QUẢN LÝ SINH VIÊN (FULL TÍNH NĂNG) ---
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 1. Form nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        
        inputPanel.add(new JLabel("Mã SV:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("Họ Tên:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(txtEmail);
        
        panel.add(inputPanel, BorderLayout.NORTH);

        // 2. Bảng danh sách
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Email"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Khu vực chức năng (Nút bấm + Tìm kiếm)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1)); // Chia làm 2 dòng

        // 3a. Các nút thao tác (Đã thêm lại nút Sắp xếp)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDel = new JButton("Xóa");
        JButton btnSort = new JButton("Sắp xếp tên"); // <--- NÚT SẮP XẾP Ở ĐÂY
        JButton btnRefresh = new JButton("Làm mới DS");
        
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); 
        btnPanel.add(btnDel); 
        btnPanel.add(btnSort); // <--- Add vào panel
        btnPanel.add(btnRefresh);

        // 3b. Khu vực tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");
        
        searchPanel.add(new JLabel("Nhập Mã hoặc Tên:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Thêm vào panel chính
        bottomPanel.add(btnPanel);
        bottomPanel.add(searchPanel);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // --- LOGIC XỬ LÝ ---
        
        // Hàm load dữ liệu
        Runnable loadData = () -> {
            model.setRowCount(0);
            for (Student s : studentService.getAll()) 
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
        };
        
        // Click bảng -> Điền dữ liệu lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtEmail.setText(model.getValueAt(row, 2).toString());
            }
        });

        // Nút Thêm
        btnAdd.addActionListener(e -> {
        	try {
                // 1. Lấy dữ liệu
                Student s = new Student(txtId.getText(), txtName.getText(), txtEmail.getText());

                // 2. Gọi hàm thêm (Không dùng IF nữa)
                // Nếu có lỗi (Email sai hoặc Trùng mã), nó sẽ nhảy ngay xuống phần catch
                studentService.addStudent(s);

                // 3. Nếu chạy đến dòng này nghĩa là thành công
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                loadData.run(); // Load lại bảng
                
                // Xóa trắng form
                txtId.setText(""); 
                txtName.setText(""); 
                txtEmail.setText("");

            } catch (Exception ex) {
                // 4. Bắt lỗi và hiện thông báo cụ thể
                // ex.getMessage() sẽ hiện: "Email không đúng..." HOẶC "Mã sinh viên đã tồn tại!"
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage());
            }
        });
        
        // Nút Sửa
        btnUpdate.addActionListener(e -> {
            try {
            	if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập Mã và Tên môn học!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // 1. Gọi hàm xử lý (Không cần if nữa)
                // Nếu có lỗi (email sai, trùng ID...), nó sẽ tự nhảy xuống catch
                studentService.updateStudent(txtId.getText(), txtName.getText(), txtEmail.getText());

                // 2. Nếu chạy đến dòng này tức là thành công
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                loadData.run(); // Tải lại bảng
                
            } catch (Exception ex) {
                // 3. Bắt lỗi: ex.getMessage() sẽ hiện ra dòng chữ bạn đã viết bên Service
                // Ví dụ: "Email không đúng định dạng" hoặc "Không tìm thấy ID"
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Nút Xóa
        btnDel.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?") == 0) {
                if(studentService.deleteStudent(txtId.getText())) {
                    JOptionPane.showMessageDialog(this, "✅ Đã xóa!");
                    loadData.run();
                } else { JOptionPane.showMessageDialog(this, "❌ Không xóa được (Có thể SV đang có điểm)."); }
            }
        });
        
        // Nút sắp xếp
        btnSort.addActionListener(e -> {
            // Gọi hàm getStudentsSortedByName trong Service (đã viết từ bước trước)
            List<Student> sortedList = studentService.getStudentsSortedByName();
            model.setRowCount(0); // Xóa bảng cũ
            for (Student s : sortedList) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
            }
            JOptionPane.showMessageDialog(this, "✅ Đã sắp xếp theo tên!");
        });
        
        // --- LOGIC TÌM KIẾM ---
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if(keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa!");
                return;
            }
            List<Student> results = studentService.searchStudent(keyword);
            model.setRowCount(0);
            if(results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên nào!");
            } else {
                for(Student s : results) {
                    model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
                }
            }
        });
        
        // Nút Làm mới
        btnRefresh.addActionListener(e -> {
            loadData.run();
            txtSearch.setText("");
            txtId.setText(""); txtName.setText(""); txtEmail.setText("");
        });
        
        loadData.run(); // Load lần đầu
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
       //nút thêm
        btnAdd.addActionListener(e -> {
            try {
                // 1. Kiểm tra dữ liệu đầu vào (Validation)
                if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập Mã và Tên môn học!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int credit = Integer.parseInt(txtCredit.getText());
                // 2. Lấy dữ liệu và tạo đối tượng
                Subject s = new Subject(txtId.getText(), txtName.getText(), credit);

                // 3. Gọi hàm logic và nhận kết quả (True/False)
                // Lưu ý: Hàm addSubject trong Service phải trả về boolean như đã sửa ở trên
                boolean isAdded = subjectService.addSubject(s);

                // 4. Kiểm tra kết quả để hiển thị thông báo
                if (isAdded) {
                    loadData.run(); // Chỉ load lại bảng khi thêm thành công
                    
                    // Reset các ô nhập liệu cho sạch đẹp
                    txtId.setText("");
                    txtName.setText("");
                    txtCredit.setText("");
                    
                    JOptionPane.showMessageDialog(this, "✅ Đã thêm môn học thành công!");
                } else {
                    // Trường hợp hàm trả về false (do trùng mã trong database)
                    JOptionPane.showMessageDialog(this, "❌ Thêm thất bại! Có thể Mã môn hoặc Tên môn học đã tồn tại.","Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                // Bắt lỗi khi nhập chữ vào ô tín chỉ
                JOptionPane.showMessageDialog(this, "❌ Tín chỉ phải là một số nguyên hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Bắt các lỗi hệ thống khác
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Có lỗi xảy ra: " + ex.getMessage());
            }
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

 // --- TAB 3: QUẢN LÝ ĐIỂM (Đã thêm hiển thị Tên Môn Học) ---
    private JPanel createGradePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 1. Form nhập điểm
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập điểm"));
        
        JTextField txtSid = new JTextField();
        JTextField txtSubId = new JTextField();
        JTextField txtScore = new JTextField();
        
        inputPanel.add(new JLabel("Mã SV:")); inputPanel.add(txtSid);
        inputPanel.add(new JLabel("Mã MH:")); inputPanel.add(txtSubId);
        inputPanel.add(new JLabel("Điểm số:")); inputPanel.add(txtScore);
        
        JButton btnSave = new JButton("Lưu Điểm");
        inputPanel.add(new JLabel("")); inputPanel.add(btnSave);
        
        panel.add(inputPanel, BorderLayout.NORTH);

        // 2. Bảng xem điểm (THÊM CỘT "Tên MH")
        String[] columns = {"Mã SV", "Mã MH", "Tên Môn Học", "Điểm"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // 3. Khu vực tìm kiếm
        JPanel searchPanel = new JPanel();
        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Xem bảng điểm SV này");
        JLabel lblGpa = new JLabel("  GPA: ...");
        lblGpa.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchPanel.add(new JLabel("Nhập mã SV:")); 
        searchPanel.add(txtSearch); 
        searchPanel.add(btnSearch);
        searchPanel.add(lblGpa);
        
        panel.add(searchPanel, BorderLayout.SOUTH);

        // --- LOGIC XỬ LÝ ---

        // Nút Lưu Điểm
        btnSave.addActionListener(e -> {
            try {
                double score = Double.parseDouble(txtScore.getText());
                if(score < 0 || score > 10) {
                    JOptionPane.showMessageDialog(this, "❌ Điểm phải từ 0 - 10!");
                    return;
                }
                gradeService.addOrUpdateGrade(txtSid.getText(), txtSubId.getText(), score);
                JOptionPane.showMessageDialog(this, "✅ Đã lưu điểm!");
                // Clear form sau khi lưu
                txtScore.setText(""); 
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "❌ Điểm không hợp lệ hoặc sai Mã!"); }
        });

        // Nút Xem Điểm (Đã sửa để lấy Tên Môn Học)
        btnSearch.addActionListener(e -> {
            String sid = txtSearch.getText().trim();
            if(sid.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã SV!");
                return;
            }

            model.setRowCount(0); // Xóa bảng cũ
            List<Grade> list = gradeService.getGradesByStudent(sid);
            
            if(list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy điểm của SV này!");
                lblGpa.setText("  GPA: ...");
            } else {
                for(Grade g : list) {
                    // Gọi SubjectService để lấy tên môn học từ ID
                    String subName = subjectService.getSubjectNameById(g.getSubjectId());
                    if(subName == null) subName = "Không rõ";
                    
                    // Thêm dòng mới có đủ 4 cột
                    model.addRow(new Object[]{
                        g.getStudentId(), 
                        g.getSubjectId(), 
                        subName, // <--- Đã thêm tên môn
                        g.getScore()
                    });
                }
                // Tính và hiển thị GPA
                double gpa = gradeService.calculateGPA(sid);
                lblGpa.setText("  GPA: " + String.format("%.2f", gpa));
                lblGpa.setForeground(Color.RED);
            }
        });

        // Sự kiện Click bảng -> Điền ngược lên form nhập (Tiện tay làm luôn cho xịn)
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                txtSid.setText(model.getValueAt(row, 0).toString());
                txtSubId.setText(model.getValueAt(row, 1).toString());
                txtScore.setText(model.getValueAt(row, 3).toString());
            }
        });

        return panel;
    }
}