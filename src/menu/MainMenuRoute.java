package menu;
import model.*;
import service.*;
import java.util.Scanner;

public class MainMenuRoute {
	
    public static void route(
        user currentUser,
        StudentService studentService,
        SubjectService subjectService,
        GradeService gradeService,
        AuthService authService,
        Scanner sc
    ) {
        switch (currentUser.getRole().toLowerCase()) {
            case "admin" ->
                new TeacherMenu(currentUser, studentService, subjectService, gradeService, authService, sc).showTeacherMenu();
            case "student" ->
                new studentMenu(currentUser, studentService, subjectService, gradeService, authService, sc).showStudentMenu();
            
        }
    }
}

