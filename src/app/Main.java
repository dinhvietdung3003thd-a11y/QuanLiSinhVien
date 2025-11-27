package app;

import java.util.*;
import model.*;
import service.*;
import menu.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        SubjectService subjectService = new SubjectService();
        GradeService gradeService = new GradeService();
        AuthService authService = new AuthService();

        // Load dữ liệu khi khởi động
        studentService.loadFromFile();
        subjectService.loadFromFile();
        gradeService.loadFromFile();
        authService.loadFromFile();
        
        user currentUser = null;

        while (currentUser == null) {
            System.out.println("\n===== ĐĂNG NHẬP / ĐĂNG KÝ =====");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int authChoice;
            try { authChoice = Integer.parseInt(sc.nextLine()); } catch (Exception e) { authChoice = -1; }

            switch (authChoice) {
                case 1 -> {
                    System.out.print("Tên đăng nhập: ");
                    String username = sc.nextLine();
                    System.out.print("Mật khẩu: ");
                    String password = sc.nextLine();

                    user u = authService.Login(username, password);
                    if (u != null) {
                        currentUser = u;
                        System.out.println("✅ Đăng nhập thành công! Xin chào " + u.getUserName());
                    } else {
                        System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
                    }
                }
                case 2 -> {
                    System.out.print("Tên đăng nhập mới: ");
                    String username = sc.nextLine();
                    boolean i = authService.CheckName(username);
                    if(i == true) {
                    System.out.print("Mật khẩu mới: ");
                    String password = sc.nextLine();
                    System.out.print("Vai trò (admin/student): ");
                    String role = sc.nextLine();
                    authService.add(username, password, role);
                    authService.saveToFile(); // lưu ngay sau khi đăng ký
                    }
                }
                case 0 -> {
                    System.out.println("💾 Thoát chương trình");
                    System.exit(0);
                }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }

        MainMenuRoute.route(currentUser, studentService, subjectService, gradeService, authService, sc);
        
       
            

            // Auto save sau mỗi thao tác
            studentService.saveToFile();
            subjectService.saveToFile();
            gradeService.saveToFile();
            authService.saveToFile();
    }
}
