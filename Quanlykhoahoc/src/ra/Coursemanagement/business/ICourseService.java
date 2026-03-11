package ra.Coursemanagement.business;

import ra.Coursemanagement.model.Course;
import java.util.List;

public interface ICourseService {
    List<Course> getAllCourses();
    Course getCourseById(int id);
    Course getCourseByName(String name);
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourse(int id);
    List<Course> searchCourses(String keyword);
    List<Course> sortCourses(String sortBy, boolean ascending);
    int getTotalCourses();
    List<Course> getTopCourses(int limit);
    List<Course> getCoursesWithMoreThan(int studentCount);
    boolean isCourseNameExists(String name);
    int generateNewCourseId();
    boolean validateCourseData(Course course);
}