package ra.coursemanagement.business;

import ra.coursemanagement.model.Student;
import java.util.List;

public interface IStudentService {
    List<Student> getAllStudents();
    Student getStudentById(int id);
    Student getStudentByEmail(String email);
    Student login(String email, String password);
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(int id);
    List<Student> searchStudents(String keyword);
    List<Student> sortStudents(String field, String order);
    boolean changePassword(int studentId, String oldPassword, String newPassword);
    int getNextId();
}