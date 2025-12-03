package view;

import model.user; // Nếu bạn đã sửa thành User viết hoa thì sửa lại chỗ này nhé
import service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    /**
	 * khai báo phiên bản 
	 */
	private static final long serialVersionUID = 1L;
	private AuthService authService = new AuthService();
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginView() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle);

        // 2. Khu vực nhập liệu
        JPanel panelInput = new JPanel(new GridLayout(2, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        
        panelInput.add(new JLabel("Tài khoản:"));
        panelInput.add(txtUsername);
        panelInput.add(new JLabel("Mật khẩu:"));
        panelInput.add(txtPassword);
        add(panelInput);

        // 3. Các nút bấm
        JPanel panelBtn = new JPanel();
        JButton btnLogin = new JButton("Đăng Nhập");
        JButton btnRegister = new JButton("Đăng Ký");
        
        panelBtn.add(btnLogin);
        panelBtn.add(btnRegister);
        add(panelBtn);
        
        // 4. Dòng thông báo trạng thái
        JLabel lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);
        add(lblStatus);

        // --- XỬ LÝ SỰ KIỆN ---
        
        // Nút Đăng Nhập
        btnLogin.addActionListener(e -> {
            String uName = txtUsername.getText();
            String pwd = new String(txtPassword.getPassword());
            
            user u = authService.Login(uName, pwd);
            
            if (u != null) {
                dispose(); // Đóng cửa sổ Login
                if (u.getRole().equalsIgnoreCase("admin")) {
                    new AdminMainView(u).setVisible(true); // Mở giao diện Admin
                } else {
                    new StudentPortalView(u).setVisible(true); // Mở giao diện Sinh viên
                }
            } else {
                lblStatus.setText("❌ Sai tài khoản hoặc mật khẩu!");
            }
        });
        

        // Nút Đăng Ký
        btnRegister.addActionListener(e -> {           
            new RegisterView().setVisible(true);
        });
    }
}