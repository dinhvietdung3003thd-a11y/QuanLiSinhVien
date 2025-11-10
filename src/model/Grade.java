package model;

public class Grade {
    private String studentId;
    private String subjectId;
    private double score;

    public Grade(String studentId, String subjectId, double score) {
        this.studentId = studentId.trim();
        this.subjectId = subjectId.trim();
        this.score = score;
    }

    public String getStudentId() { return studentId; }
    public String getSubjectId() { return subjectId; }
    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    @Override
    public String toString() {
        return studentId + "," + subjectId + "," + score;
    }
}
