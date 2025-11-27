package menu;
import java.util.*;
import model.*;
import service.*;

public class studentMenu {
	private user currentUser;
    private StudentService studentService;
    private SubjectService subjectService;
    private GradeService gradeService;
    private AuthService authService;
    private Scanner sc;

    public studentMenu (
        user currentUser,
        StudentService studentService,
        SubjectService subjectService,
        GradeService gradeService,
        AuthService authService,
        Scanner sc
    ) {
        this.currentUser = currentUser;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.gradeService = gradeService;
        this.authService = authService;
        this.sc = sc;
    }

	public void showStudentMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Xem bảng điểm của sinh viên");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            try { choice = Integer.parseInt(sc.nextLine()); } catch (Exception e) { choice = -1; }    
      
            switch (choice) {
                
                case 1 -> {
                	String sid = currentUser.getUserName();  // <-- CHỈ SỬ DỤNG username (studentId)
                    Student student = studentService.findStudent(sid);

                    if (student == null) {
                        System.out.println("❌ Không tìm thấy thông tin sinh viên!");
                        break;
                    }

                    List<Grade> list = gradeService.getGradesByStudent(sid);

                    if (list.isEmpty()) {
                        System.out.println("❌ Sinh viên chưa có điểm!");
                    } else {
                        System.out.println("\n📘 BẢNG ĐIỂM CỦA: " + student.getName() + " (" + sid + ")");
                        System.out.println("-------------------------------------------------");
                        System.out.printf("%-10s %-30s %s%n", "Mã MH", "Tên môn học", "Điểm");

                        for (Grade g : list) {
                            Subject sub = subjectService.findSubject(g.getSubjectId());
                            String subName = (sub != null) ? sub.getName() : "❓Không tìm thấy";
                            System.out.printf("%-10s %-30s %.2f%n", 
                                g.getSubjectId(), subName, g.getScore());
                        }

                        System.out.println("-------------------------------------------------");
                        System.out.printf("🎯 GPA: %.2f%n", gradeService.calculateGPA(sid));
                    }
                }	
                case 2 -> {
                    int attempts = 0;
                    final int MAX_ATTEMPTS = 3;
                    boolean changeSuccessful = false;
                    while (attempts < MAX_ATTEMPTS) {
                        System.out.print("Nhập mật khẩu cũ: ");
                        String oldPass = sc.nextLine();
                        if (authService.check(currentUser, oldPass)) {
                            System.out.print("Nhập mật khẩu mới: ");
                            String newPass = sc.nextLine();
                            authService.changePassword(currentUser, newPass);
                            changeSuccessful = true;
                            break; 
                        } else {
                            attempts++;
                            int remainingAttempts = MAX_ATTEMPTS - attempts;
                            if (remainingAttempts > 0) {
                                System.out.println("Sai mật khẩu! Vui lòng thử lại. Bạn còn " + remainingAttempts + " lần thử.");
                            }
                        }
                    }
                    
                    // Thông báo khi hết số lần thử
                    if (!changeSuccessful) {
                        System.out.println("Bạn đã nhập sai quá 3 lần. Không thể đổi mật khẩu!");
                    }
                }

                case 0 -> System.out.println("💾 Thoát chương trình, dữ liệu đã lưu!");
                default -> System.out.println("lựa chọn không hợp lệ ! vui lòng nhập lại !");
            }            
        } while (choice != 0);
    }
}