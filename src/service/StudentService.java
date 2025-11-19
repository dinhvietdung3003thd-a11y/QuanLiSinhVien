package service;

import model.Student;
import util.FileUtils;
import java.util.*;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public void CheckID(String id) {
    	
    }
    
    public void addStudent(Student s) {
        students.add(s);
        System.out.println("✅ Thêm sinh viên thành công!");
    }

    public void updateStudent(String id, String newName, String newEmail) {
        Student s = findStudent(id);
            s.setName(newName);
            s.setEmail(newEmail);
            System.out.println("✅ Cập nhật thành công!");
    }

    public void deleteStudent(String id) {
        Student s = findStudent(id);
            students.remove(s);
            System.out.println("✅ Đã xóa!");
    }
    
    public Student findStudentbyName(String name) {
    	for(Student s : students) {
    		if (s.getName().equalsIgnoreCase(name)) {
    			return s;
    		}
    		
    	}
    	return null;
    }

    public Student findStudent(String id) {
        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }
    
    public String getStudentNameById(String id) {
        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s.getName();
            }
        }
        return null;
    }


    public void showAll() {
        if (students.isEmpty()) {
            System.out.println("❌ Chưa có sinh viên nào!");
            return;
        }
        students.forEach(System.out::println);
    }

    public void sortByName() {
        Collections.sort(students);
        System.out.println("✅ Đã sắp xếp theo tên!");
    }

    public List<Student> getAll() { return students; }

    // ==== File I/O ====
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Student s : students)
            lines.add(s.getId() + "," + s.getName() + "," + s.getEmail());
        FileUtils.writeFile("students.csv", lines);
    }

    public void loadFromFile() {
        List<String> lines = FileUtils.readFile("students.csv");
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length >= 3 && findStudent(p[0]) == null)
                students.add(new Student(p[0], p[1], p[2]));
        }
    }
}
