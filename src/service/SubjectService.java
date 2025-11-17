package service;

import model.Subject;
import util.FileUtils;
import java.util.*;

public class SubjectService {
    private List<Subject> subjects = new ArrayList<>();

    public void addSubject(Subject s) {
        if (findSubject(s.getId()) != null) {
            System.out.println("⚠️ ID môn học đã tồn tại!");
            return;
        }
        subjects.add(s);
        System.out.println("✅ Thêm môn học thành công!");
    }

    public void deleteSubject(String id) {
        Subject s = findSubject(id);
        if (s != null) {
            subjects.remove(s);
            System.out.println("✅ Đã xóa môn học!");
        } else {
            System.out.println("❌ Không tìm thấy môn học!");
        }
    }

    public Subject findSubject(String id) {
        for (Subject s : subjects) {
            if (s.getId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }
    
    public String getSubjectNameById(String id) {
        for (Subject s : subjects) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s.getName();
            }
        }
        return "Không rõ môn";
    }


    public void showAll() {
        if (subjects.isEmpty()) {
            System.out.println("❌ Chưa có môn học nào!");
            return;
        }
        subjects.forEach(System.out::println);
    }

    // ==== File I/O ====
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Subject s : subjects)
            lines.add(s.getId() + "," + s.getName() + "," + s.getCredits());
        FileUtils.writeFile("subjects.csv", lines);
    }

    public void loadFromFile() {
        List<String> lines = FileUtils.readFile("subjects.csv");
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length >= 3 && findSubject(p[0]) == null)
                subjects.add(new Subject(p[0], p[1], Integer.parseInt(p[2])));
        }
    }

    public List<Subject> getAll() { return subjects; }
}
