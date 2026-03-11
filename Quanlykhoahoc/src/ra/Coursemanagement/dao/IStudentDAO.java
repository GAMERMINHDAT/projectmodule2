package ra.Coursemanagement.dao;

import ra.Coursemanagement.model.Student;
import java.util.List;

public interface IStudentDAO {
    List<Student> getAllStudents();
    Student getStudentById(int id);
    Student getStudentByEmail(String email);
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(int id);
    List<Student> searchStudents(String keyword); // Tìm theo tên, email, id
    List<Student> sortStudents(String sortBy, boolean ascending); // sortBy: "name" or "id"
    int getTotalStudents();
    boolean updatePassword(int studentId, String newPassword);
    boolean checkLogin(String email, String password);
}