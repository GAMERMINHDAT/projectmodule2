package ra.coursemanagement.business;

import ra.coursemanagement.model.Course;
import java.util.List;

public interface ICourseService {
    List<Course> getAllCourses();
    Course getCourseById(int id);
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourse(int id);
    List<Course> searchCoursesByName(String name);
    List<Course> sortCourses(String field, String order);
    int getTotalCourses();
    int getNextId();
}