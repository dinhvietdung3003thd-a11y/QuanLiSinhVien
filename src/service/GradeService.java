package service;

import model.Grade;
import util.FileUtils;
import java.util.*;

public class GradeService {
    private List<Grade> grades = new ArrayList<>();

    public void addOrUpdateGrade(String sid, String subid, double score) {
        for (Grade g : grades) {
            if (g.getStudentId().equalsIgnoreCase(sid) && g.getSubjectId().equalsIgnoreCase(subid)) {
                g.setScore(score);
                System.out.println("✅ Đã cập nhật điểm!");
                return;
            }
        }
        grades.add(new Grade(sid, subid, score));
        System.out.println("✅ Nhập điểm thành công!");
    }

    public List<Grade> getGradesByStudent(String sid) {
        List<Grade> list = new ArrayList<>();
        for (Grade g : grades)
            if (g.getStudentId().equalsIgnoreCase(sid))
                list.add(g);
        return list;
    }
    
    public void showGradesByStudent(String studentId, StudentService studentService, SubjectService subjectService) {
        String studentName = studentService.getStudentNameById(studentId);
        if (studentName == null) {
            System.out.println("❌ Không tìm thấy sinh viên có ID: " + studentId);
            return;
        }

        System.out.println("\nBảng điểm của sinh viên: " + studentName);
        System.out.println("---------------------------------------");

        boolean found = false;
        for (Grade g : grades) {
            if (g.getStudentId().equalsIgnoreCase(studentId)) {
                String subjectName = subjectService.getSubjectNameById(g.getSubjectId());
                System.out.println(subjectName + ": " + g.getScore());
                found = true;
            }
        }

        if (!found) {
            System.out.println("⚠️ Sinh viên này chưa có điểm!");
        }

        System.out.println("---------------------------------------");
    }

    public double calculateGPA(String sid) {
        List<Grade> list = getGradesByStudent(sid);
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (Grade g : list) sum += g.getScore();
        return sum / list.size();
    }

    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Grade g : grades)
            lines.add(g.getStudentId() + "," + g.getSubjectId() + "," + g.getScore());
        FileUtils.writeFile("grades.csv", lines);
    }

    public void loadFromFile() {
        List<String> lines = FileUtils.readFile("grades.csv");
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length >= 3)
                grades.add(new Grade(p[0], p[1], Double.parseDouble(p[2])));
        }
    }
}
