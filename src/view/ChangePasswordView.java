package view;

import model.user;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordView extends JFrame {
    /**
	 * số định dạng để đọc đunugs phiên bản 
	 */
	private static final long serialVersionUID = 1L;
	private AuthService authService = new AuthService();
    private user currentUser;

    private JPasswordField txtOldPass;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;

    public ChangePasswordView(user u) {
        this.currentUser = u;
        setTitle("Đổi Mật Khẩu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng cửa sổ này
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        txtOldPass = new JPasswordField();
        txtNewPass = new JPasswordField();
        txtConfirmPass = new JPasswordField();

        panelInput.add(new JLabel("Mật khẩu cũ:"));
        panelInput.add(txtOldPass);
        
        panelInput.add(new JLabel("Mật khẩu mới:"));
        panelInput.add(txtNewPass);

        panelInput.add(new JLabel("Xác nhận mới:"));
        panelInput.add(txtConfirmPass);

        add(panelInput, BorderLayout.CENTER);

        // Nút bấm
        JPanel panelBtn = new JPanel();
        JButton btnSave = new JButton("Lưu Thay Đổi");
        JButton btnCancel = new JButton("Hủy");

        btnSave.setBackground(new Color(30, 144, 255));
        btnSave.setForeground(Color.WHITE);

        panelBtn.add(btnSave);
        panelBtn.add(btnCancel);
        add(panelBtn, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---

        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            String oldPass = new String(txtOldPass.getPassword());
            String newPass = new String(txtNewPass.getPassword());
            String confirm = new String(txtConfirmPass.getPassword());

            // 1. Kiểm tra rỗng
            if (oldPass.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // 2. Kiểm tra mật khẩu cũ có đúng không
            // (Hàm check này lấy từ AuthService mình đã gửi ở bước trước)
            if (!authService.check(currentUser, oldPass)) {
                JOptionPane.showMessageDialog(this, "❌ Mật khẩu cũ không đúng!");
                return;
            }

            // 3. Kiểm tra mật khẩu mới trùng khớp
            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "❌ Mật khẩu xác nhận không khớp!");
                return;
            }

            // 4. Tiến hành đổi
            authService.changePassword(currentUser, newPass);
            JOptionPane.showMessageDialog(this, "✅ Đổi mật khẩu thành công!");
            dispose();
        });
    }
}