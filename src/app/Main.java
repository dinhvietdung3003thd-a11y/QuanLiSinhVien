package app;

import java.util.*;
import model.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        SubjectService subjectService = new SubjectService();
        GradeService gradeService = new GradeService();

        // Load dữ liệu khi khởi động
        studentService.loadFromFile();
        subjectService.loadFromFile();
        gradeService.loadFromFile();

        int choice;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Quản lý sinh viên");
            System.out.println("2. Quản lý môn học");
            System.out.println("3. Nhập/Sửa điểm");
            System.out.println("4. Xem bảng điểm");
            System.out.println("5. Sắp xếp theo tên / GPA");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Quản lý sinh viên ---");
                    System.out.println("1. Thêm");
                    System.out.println("2. Sửa");
                    System.out.println("3. Xóa");
                    System.out.println("4. tìm kiếm");
                    System.out.println("5. Hiển thị tất cả");
                    System.out.print("Chọn: ");
                    int c = Integer.parseInt(sc.nextLine());
                    switch (c) {
                        case 1 -> {
                            System.out.print("Mã SV: "); String id = sc.nextLine();
                            System.out.print("Tên: "); String name = sc.nextLine();
                            System.out.print("Email: "); String email = sc.nextLine();
                            studentService.addStudent(new Student(id, name, email));
                        }
                        case 2 -> {
                            System.out.print("Mã SV cần sửa: "); String id = sc.nextLine();
                            System.out.print("Tên mới: "); String name = sc.nextLine();
                            System.out.print("Email mới: "); String email = sc.nextLine();
                            studentService.updateStudent(id, name, email);
                        }
                        case 3 -> {
                            System.out.print("Mã SV cần xóa: "); String id = sc.nextLine();
                            studentService.deleteStudent(id);
                        }
                        case 4 -> {
                        		   System.out.println("\n----tìm kiếm----");
                        		   System.out.println("1. Theo tên");
                        		   System.out.println("2. Theo Mã SV");
                        		   int d = Integer.parseInt(sc.nextLine());
                               	switch (d) {
                               	   case 1 -> {
                               		   System.out.print("tên sinh viên cần tìm: ");  String name = sc.nextLine();
                               		   Student s = studentService.findStudentbyName(name);
                               		   if(s != null) {
                               			   System.out.print(s);
                               		   } else {
                               			   System.out.println("không có sinh viên cần tìm !");
                               		   }
                               		   
                               	   }
                               	   case 2 -> {
                               		   System.out.print("ma sinh vien can tim: "); String id = sc.nextLine();
                               		   Student s = studentService.findStudent(id);
                               		   if(s != null) {
                             			   System.out.print(s);
                             		   } else {
                             			   System.out.println("không có sinh viên cần tìm !");
                             		   }
                               	   }
                        	   }
                        	}
                        case 5 -> studentService.showAll();
                    }
                }
      
                case 2 -> {
                    System.out.println("\n--- Quản lý môn học ---");
                    System.out.println("1. Thêm môn");
                    System.out.println("2. Xóa môn");
                    System.out.println("3. Hiển thị tất cả");
                    System.out.print("Chọn: ");
                    int c = Integer.parseInt(sc.nextLine());
                    switch (c) {
                        case 1 -> {
                            System.out.print("Mã MH: "); String id = sc.nextLine();
                            System.out.print("Tên MH: "); String name = sc.nextLine();
                            System.out.print("Số tín chỉ: "); int credits = Integer.parseInt(sc.nextLine());
                            subjectService.addSubject(new Subject(id, name, credits));
                        }
                        case 2 -> {
                            System.out.print("Mã MH cần xóa: "); String id = sc.nextLine();
                            subjectService.deleteSubject(id);
                        }
                        case 3 -> subjectService.showAll();
                    }
                }

                case 3 -> {
                    System.out.print("Mã SV: "); String sid = sc.nextLine();
                    System.out.print("Mã MH: "); String subid = sc.nextLine();
                    System.out.print("Điểm: "); double score = Double.parseDouble(sc.nextLine());
                    gradeService.addOrUpdateGrade(sid, subid, score);
                }

                case 4 -> {
                    System.out.print("Nhập mã SV cần xem: ");
                    String sid = sc.nextLine();
                    Student student = studentService.findStudent(sid);
                    if (student == null) {
                        System.out.println("❌ Không tìm thấy sinh viên!");
                        break;
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
                }

                case 5 -> {
                    System.out.println("1. Sắp xếp theo tên");
                    System.out.println("2. Sắp xếp theo GPA");
                    int c = Integer.parseInt(sc.nextLine());
                    if (c == 1) {
                        studentService.sortByName();
                        studentService.showAll();
                    } else if (c == 2) {
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
                }

                case 0 -> System.out.println("💾 Thoát chương trình, dữ liệu đã lưu!");
            }

            // Auto save sau mỗi thao tác
            studentService.saveToFile();
            subjectService.saveToFile();
            gradeService.saveToFile();

        } while (choice != 0);
    }
}
