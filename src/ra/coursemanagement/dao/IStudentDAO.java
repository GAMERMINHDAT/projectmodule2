package ra.coursemanagement.dao;

import ra.coursemanagement.model.Student;
import java.util.List;

public interface IStudentDAO {
    List<Student> findAll();
    Student findById(int id);
    Student findByEmail(String email);
    Student findByUsernameAndPassword(String username, String password);
    boolean add(Student student);
    boolean update(Student student);
    boolean delete(int id);
    List<Student> search(String keyword);
    List<Student> sort(String field, String order);
}