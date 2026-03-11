package ra.Coursemanagement.model;

import java.time.LocalDateTime;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private LocalDateTime registeredAt;
    private String status; // WAITING, DENIED, CANCEL, CONFIRMED

    // Additional fields for display
    private String studentName;
    private String courseName;

    public Enrollment() {
    }

    public Enrollment(int id, int studentId, int courseId, LocalDateTime registeredAt, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registeredAt = registeredAt;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatusInVietnamese() {
        switch (status) {
            case "WAITING": return "Chờ duyệt";
            case "CONFIRMED": return "Đã xác nhận";
            case "DENIED": return "Từ chối";
            case "CANCEL": return "Đã hủy";
            default: return status;
        }
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", registeredAt=" + registeredAt +
                ", status='" + status + '\'' +
                '}';
    }
}