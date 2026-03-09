package ra.coursemanagement.model;

import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private LocalDate dob;
    private String email;
    private int sex; // 0: Nữ, 1: Nam
    private String phone;
    private String password;
    private LocalDate createAt;

    public Student() {}

    public Student(int id, String name, LocalDate dob, String email, int sex, String phone, String password, LocalDate createAt) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
        this.password = password;
        this.createAt = createAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getSex() { return sex; }
    public void setSex(int sex) { this.sex = sex; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDate getCreateAt() { return createAt; }
    public void setCreateAt(LocalDate createAt) { this.createAt = createAt; }

    public String getSexString() {
        return sex == 1 ? "Nam" : "Nữ";
    }
}