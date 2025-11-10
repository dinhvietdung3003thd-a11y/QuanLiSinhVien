package model;

public class Student implements Comparable<Student> {
    private String id;
    private String name;
    private String email;

    public Student(String id, String name, String email) {
        this.id = id.trim();
        this.name = name.trim();
        this.email = email.trim();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public int compareTo(Student o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", id, name, email);
    }
}
