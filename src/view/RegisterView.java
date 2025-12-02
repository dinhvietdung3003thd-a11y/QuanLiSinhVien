package view;

import service.AuthService;
import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private AuthService authService = new AuthService();

    private JTextField txtUser;
    private JPasswordField txtPass, txtConfirmPass;
    private JComboBox<String> cbRole; // Chọn quyền (Admin/Student)

    public RegisterView() {
        setTitle("Cấp Tài Khoản Mới");
        setSize(400, 350); // Gọn nhẹ hơn
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu (Chỉ gồm Username, Password, Role)
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        txtConfirmPass = new JPasswordField();
        
        // ComboBox chọn quyền
        String[] roles = {"student", "admin"};
        cbRole = new JComboBox<>(roles);

        panelInput.add(new JLabel("Tên đăng nhập:"));
        panelInput.add(txtUser);
        
        panelInput.add(new JLabel("Mật khẩu:"));
        panelInput.add(txtPass);

        panelInput.add(new JLabel("Nhập lại mật khẩu:"));
        panelInput.add(txtConfirmPass);
        
        panelInput.add(new JLabel("Vai trò:"));
        panelInput.add(cbRole);

        add(panelInput, BorderLayout.CENTER);

        // Nút bấm
        JPanel panelBtn = new JPanel();
        JButton btnRegister = new JButton("Tạo Tài Khoản");
        JButton btnCancel = new JButton("Hủy");
        
        btnRegister.setBackground(new Color(0, 153, 76)); // Màu xanh lá
        btnRegister.setForeground(Color.WHITE);

        panelBtn.add(btnRegister);
        panelBtn.add(btnCancel);
        add(panelBtn, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---

        btnCancel.addActionListener(e -> dispose());

        btnRegister.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirmPass.getPassword());
            String role = (String) cbRole.getSelectedItem();

            // 1. Kiểm tra rỗng
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
                return;
            }

            // 2. Kiểm tra mật khẩu khớp
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            // 3. Gọi Service để lưu vào bảng USERS (Chỉ lưu user, pass, role)
            if (authService.add(user, pass, role)) {
                JOptionPane.showMessageDialog(this, "✅ Tạo tài khoản thành công!");
                dispose(); // Đóng form
            } else {
                JOptionPane.showMessageDialog(this, "❌ Tên đăng nhập đã tồn tại!");
            }
        });
    }
}