package ra.Coursemanagement.business;

import ra.Coursemanagement.model.Student;
import java.util.List;

public interface IStudentService {
    List<Student> getAllStudents();
    Student getStudentById(int id);
    Student getStudentByEmail(String email);
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(int id);
    List<Student> searchStudents(String keyword);
    List<Student> sortStudents(String sortBy, boolean ascending);
    int getTotalStudents();
    boolean updatePassword(int studentId, String oldPassword, String newPassword);
    boolean checkLogin(String email, String password);
    boolean isEmailExists(String email);
    int generateNewStudentId();
    boolean validateStudentData(Student student);
}