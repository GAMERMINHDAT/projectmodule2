package ra.coursemanagement.dao;

import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Enrollment;
import java.util.List;
import java.util.Map;

public interface IEnrollmentDAO {
    List<Enrollment> findAll();
    List<Enrollment> findByStudentId(int studentId);
    List<Enrollment> findByCourseId(int courseId);
    Map<Course, Long> getStudentCountByCourse(); // Thêm method này
    boolean add(Enrollment enrollment);
    boolean updateStatus(int id, String status);
    boolean delete(int id);
    boolean exists(int studentId, int courseId);
    List<Course> getTopCourses(int limit); // Thêm method này
    List<Course> getCoursesWithMoreThan10Students(); // Thêm method này
}