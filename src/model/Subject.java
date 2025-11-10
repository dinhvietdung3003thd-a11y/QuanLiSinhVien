package model;

public class Subject {
    private String id;
    private String name;
    private int credits;

    public Subject(String id, String name, int credits) {
        this.id = id.trim();
        this.name = name.trim();
        this.credits = credits;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCredits() { return credits; }

    public void setName(String name) { this.name = name; }
    public void setCredits(int credits) { this.credits = credits; }

    @Override
    public String toString() {
        return String.format("%s - %s (%d tín chỉ)", id, name, credits);
    }
}
