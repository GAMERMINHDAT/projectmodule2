package ra.coursemanagement.business;

import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Enrollment;
import ra.coursemanagement.model.Student;
import java.util.List;
import java.util.Map;

public interface IEnrollmentService {
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getEnrollmentsByStudent(int studentId);
    List<Student> getStudentsByCourse(int courseId);
    boolean registerCourse(int studentId, int courseId);
    boolean updateEnrollmentStatus(int enrollmentId, String status);
    boolean cancelEnrollment(int enrollmentId);
    Map<Course, Long> getStudentCountByCourse(); // Thêm method này
    List<Course> getTopCourses(int limit); // Thêm method này
    List<Course> getCoursesWithMoreThan10Students(); // Thêm method này
    int getNextId();
}