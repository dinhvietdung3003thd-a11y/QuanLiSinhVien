package menu;

import java.util.*;
import model.*;
import service.*;

public class TeacherMenu {
	private user currentUser;
    private StudentService studentService;
    private SubjectService subjectService;
    private GradeService gradeService;
    private AuthService authService;
    private Scanner sc;

    public TeacherMenu (
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

	public void showTeacherMenu() {
		studentService.loadFromFile();
        subjectService.loadFromFile();
        gradeService.loadFromFile();
        authService.loadFromFile();
        int choice;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Quản lý sinh viên");
            System.out.println("2. Quản lý môn học");
            System.out.println("3. Nhập/Sửa điểm");
            System.out.println("4. Xem bảng điểm của sinh viên");
            System.out.println("5. Sắp xếp theo tên / GPA");
            System.out.println("6. Đổi mật khẩu");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            try { choice = Integer.parseInt(sc.nextLine()); } catch (Exception e) { choice = -1; }    
      
            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Quản lý sinh viên ---");
                    System.out.println("1. Thêm");
                    System.out.println("2. Sửa");
                    System.out.println("3. Xóa");
                    System.out.println("4. tìm kiếm");
                    System.out.println("5. Hiển thị tất cả");
                    System.out.println("6. thoát.");
                    System.out.print("Chọn: ");
                    int c;
                    try { c = Integer.parseInt(sc.nextLine()); } catch (Exception e) { c = -1; }
                    
                    switch (c) {
                       case 1 -> { 
                    	   String id;
                    	    while (true) {
                    	            System.out.print("Mã SV: "); 
                    	            id = sc.nextLine();
                    	            if (studentService.findStudent(id) != null) { 
                    	                System.out.println("⚠️ Mã SV đã tồn tại! Vui lòng nhập mã khác.");
                    	                System.out.print("Bạn có muốn thử thêm Mã MH khác (y/n)? "); 
                                        String opt = sc.nextLine(); 
                                        if (!opt.equalsIgnoreCase("y")) {
                                            break; 
                                        }
                                        continue;
                    	            } 
                    	       
                    	        System.out.print("Tên: "); 
                    	        String name = sc.nextLine();
                    	        System.out.print("Email: "); 
                    	        String email = sc.nextLine();
                    	        studentService.addStudent(new Student(id, name, email));

                    	        System.out.print("Bạn có muốn thêm sinh viên khác không (y/n)? ");
                    	        String opt = sc.nextLine();
                    	        
                    	        if (!opt.equalsIgnoreCase("y")) {
                    	            break;
                    	        }
                    	    }
                       }
                        case 2 -> { 
                        	String id;
                            while (true) { 
                                    System.out.print("Mã SV cần sửa: ");
                                    id = sc.nextLine();
                                    if (studentService.findStudent(id) == null) { 
                                        System.out.println("❌ Không tìm thấy Mã SV!");
                                        System.out.print("Bạn có muốn thử sửa Mã MH khác (y/n)? "); 
                                        String opt = sc.nextLine(); 
                                        if (!opt.equalsIgnoreCase("y")) {
                                            break; 
                                        }
                                        continue;
                                    }
                    	                              
                                System.out.print("Tên mới: ");
                                String name = sc.nextLine();
                                System.out.print("Email mới: ");
                                String email = sc.nextLine();
                                
                                studentService.updateStudent(id, name, email);

                                System.out.print("Bạn có muốn sửa sinh viên khác không (y/n)? ");
                                String opt = sc.nextLine();

                                if (!opt.equalsIgnoreCase("y")) {
                                    break;
                                }
                            }
                        }
                        case 3 -> { 
                            while (true) { 
                                System.out.print("Mã SV cần xóa: ");
                                String id = sc.nextLine();
                                if (studentService.findStudent(id) == null) { 
                                    System.out.println("❌ Không tìm thấy Mã SV!");
                                    System.out.print("Bạn có muốn xóa sinh viên khác không (y/n)? ");
                                    String opt = sc.nextLine(); 
                                    if (!opt.equalsIgnoreCase("y")) {
                                        break; 
                                    }
                                    continue; 
                                } 
                                studentService.deleteStudent(id);
                                System.out.print("Bạn có muốn xóa sinh viên khác không (y/n)? ");
                                String opt = sc.nextLine();

                                if (!opt.equalsIgnoreCase("y")) {
                                    break;
                                }
                            }
                        }
                        case 4 -> {
                        		   System.out.println("\n----tìm kiếm----");
                        		   System.out.println("1. Theo tên");
                        		   System.out.println("2. Theo Mã SV");
                        		   System.out.print("Chọn: ");
                        		   int d;
                        		   try { d = Integer.parseInt(sc.nextLine()); } catch (Exception e) { d = -1; }
                               	switch (d) {
                               	   case 1 -> {
                               		   while(true) {
                               			   System.out.print("tên sinh viên cần tìm: ");  String name = sc.nextLine();
                               		       Student s = studentService.findStudentbyName(name);
                               		       if(s != null) {
                               		    	   System.out.print(s);
                               		       } else {
                               			       System.out.println("không có sinh viên cần tìm !");
                               		       }
                               		       System.out.print("\nBạn có muốn xem sinh viên khác? (y/n): ");
                                           String opt = sc.nextLine();
                                           if (!opt.equalsIgnoreCase("y")) {
                                               break; 
                                           }
                               		   }
                               		   
                               	   }
                               	   case 2 -> {
                               		   while(true) {
                               			   System.out.print("ma sinh vien can tim: "); String id = sc.nextLine();
                               			   Student s = studentService.findStudent(id);
                            		       if(s != null) {
                            		    	   System.out.print(s);
                            		       } else {
                            			       System.out.println("không có sinh viên cần tìm !");
                            		       }
                            		       System.out.print("\nBạn có muốn xem sinh viên khác? (y/n): ");
                                           String opt = sc.nextLine();
                                           if (!opt.equalsIgnoreCase("y")) {
                                               break;   	
                                           }
                            		   }
                               	   }
                               	   default -> System.out.println("lựa chọn không hợp lệ ! vui lòng nhập lại !");
                        	   }
                        	}
                        case 5 -> studentService.showAll();
                        default -> System.out.println("lựa chọn không hợp lệ ! vui lòng nhập lại !");
                    }
                }
      
                case 2 -> {
                    System.out.println("\n--- Quản lý môn học ---");
                    System.out.println("1. Thêm môn");
                    System.out.println("2. Xóa môn");
                    System.out.println("3. Hiển thị tất cả");
                    System.out.print("Chọn: ");
                    int c;
                    try { c = Integer.parseInt(sc.nextLine()); } catch (Exception e) { c = -1; }
                    switch (c) {
                        case 1 -> {
                            while(true) {
                                String ID;
                               
                                    System.out.print("Mã MH: "); 
                                    ID = sc.nextLine();
                                    if (subjectService.findSubject(ID) != null) { 
                                        System.out.println("⚠️ Mã MH đã tồn tại! Vui lòng nhập mã khác.");
                                        System.out.print("Bạn có muốn thử xóa Mã MH khác (y/n)? "); 
                                        String opt = sc.nextLine(); 
                                        if (!opt.equalsIgnoreCase("y")) {
                                            break;
                                        }
                                        continue;
                                    } 
                                System.out.print("Tên MH: "); 
                                String name = sc.nextLine();
                                System.out.print("Số tín chỉ: "); 
                                int credits = Integer.parseInt(sc.nextLine());
                                
                                subjectService.addSubject(new Subject(ID, name, credits)); 
                                
                                System.out.print("Bạn có muốn thêm môn học khác không (y/n)? "); 
                                String opt = sc.nextLine(); 
                                if (!opt.equalsIgnoreCase("y")) {
                                    break; 
                                }
                            }
                        }
                        case 2 -> {
                            while(true) {
                                System.out.print("Mã MH cần xóa: "); 
                                String id = sc.nextLine();
                                if (subjectService.findSubject(id) == null) { 
                                    System.out.println("❌ Không tìm thấy Mã MH! Vui lòng nhập mã khác.");
                                    System.out.print("Bạn có muốn thử xóa Mã MH khác (y/n)? "); 
                                    String opt = sc.nextLine(); 
                                    if (!opt.equalsIgnoreCase("y")) {
                                        break;
                                    }
                                    continue;
                                } 
                                subjectService.deleteSubject(id);
                                System.out.print("Bạn có muốn xoá môn học khác không (y/n)? "); 
                                String opt = sc.nextLine(); 
                                
                                if (!opt.equalsIgnoreCase("y")) {
                                    break; 
                                }
                            }
                        }
                        case 3 -> subjectService.showAll();
                        default -> System.out.println("lựa chọn không hợp lệ ! vui lòng nhập lại !");
                    }
                }

                case 3 -> {
                    System.out.print("Mã SV: "); String sid = sc.nextLine();
                    System.out.print("Mã MH: "); String subid = sc.nextLine();
                    System.out.print("Điểm: "); double score = Double.parseDouble(sc.nextLine());
                    gradeService.addOrUpdateGrade(sid, subid, score);
                }

                case 4 -> {
                	while(true) {
                        System.out.print("Nhập mã SV cần xem: ");
                        String sid = sc.nextLine();
                        Student student = studentService.findStudent(sid);
                        if (student == null) {
                        	System.out.println("❌ Không tìm thấy sinh viên!");
                        	continue;
                            }

                        List<Grade> list = gradeService.getGradesByStudent(sid);
                        if (list.isEmpty()) {
                        	System.out.println("❌ Sinh viên này chưa có điểm!");
                        } else {
                            System.out.println("\n📘 BẢNG ĐIỂM CỦA: " + student.getName() + " (" + sid + ")");
                            System.out.println("-------------------------------------------------");
                            System.out.printf("%-10s %-30s %s%n", "Mã MH", "Tên môn học", "Điểm");

                            for (Grade g : list) {
                            	Subject sub = subjectService.findSubject(g.getSubjectId());
                                String subName = (sub != null) ? sub.getName() : "❓Không tìm thấy";
                                System.out.printf("%-10s %-30s %.2f%n", g.getSubjectId(), subName, g.getScore());
                            }
                                System.out.println("-------------------------------------------------");
                                System.out.printf("🎯 GPA: %.2f%n", gradeService.calculateGPA(sid));
                         }
                        System.out.print("\nBạn có muốn xem sinh viên khác? (y/n): ");
                        String opt = sc.nextLine();
                        if (!opt.equalsIgnoreCase("y")) {
                            break;  // thoát về menu
                        }
                	}
                }

                case 5 -> {
                    System.out.println("1. Sắp xếp theo tên");
                    System.out.println("2. Sắp xếp theo GPA");
                    int c;
                    try { c = Integer.parseInt(sc.nextLine()); } catch (Exception e) { c = -1; }
                    switch(c) {
                        case 1 ->{
                           studentService.sortByName();
                           studentService.showAll();
                        }
                        case 2 ->{
                            List<Student> students = studentService.getAll();
                            students.sort((a, b) -> {
                                double gpaA = gradeService.calculateGPA(a.getId());
                                double gpaB = gradeService.calculateGPA(b.getId());
                                return Double.compare(gpaB, gpaA);
                            });
                            students.forEach(s ->
                                    System.out.printf("%s - %s - GPA: %.2f%n",
                                            s.getId(), s.getName(),
                                            gradeService.calculateGPA(s.getId())));
                        }
                        default -> System.out.println("lựa chọn không hợp lệ ! vui lòng nhập lại !");
                    }
                }
                
                case 6 -> {
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
            studentService.saveToFile();
            subjectService.saveToFile();
            gradeService.saveToFile();
            authService.saveToFile();
        } while (choice != 0);
    }
}
