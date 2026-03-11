package ra.Coursemanagement.business;

import ra.Coursemanagement.model.Course;
import ra.Coursemanagement.model.Enrollment;
import java.util.List;

public interface IEnrollmentService {
    List<Enrollment> getAllEnrollmentsWithDetails();
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
    List<Enrollment> getEnrollmentsByCourseId(int courseId);
    List<Enrollment> getStudentEnrollmentsWithCourseInfo(int studentId);
    boolean registerCourse(int studentId, int courseId);
    boolean updateEnrollmentStatus(int enrollmentId, String status);
    boolean cancelEnrollment(int enrollmentId);
    boolean cancelEnrollment(int studentId, int courseId);
    boolean deleteEnrollment(int id);
    boolean isStudentEnrolled(int studentId, int courseId);
    int getStudentCountByCourseId(int courseId);
    List<Object[]> getStudentCountPerCourse();
    int getTotalConfirmedStudents();
    int generateNewEnrollmentId();

    // Thống kê
    int getTotalCourses();
    int getTotalStudents();
    List<Object[]> getStudentCountPerCourseForStats();
    List<Course> getTop5Courses();
    List<Course> getCoursesWithMoreThan10Students();
}