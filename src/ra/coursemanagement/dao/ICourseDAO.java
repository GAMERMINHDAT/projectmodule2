package ra.coursemanagement.dao;

import ra.coursemanagement.model.Course;
import java.util.List;

public interface ICourseDAO {
    List<Course> findAll();
    Course findById(int id);
    boolean add(Course course);
    boolean update(Course course);
    boolean delete(int id);
    List<Course> searchByName(String name);
    List<Course> sort(String field, String order);
    int getTotalCourses();
}