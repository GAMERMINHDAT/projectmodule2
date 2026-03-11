package ra.Coursemanagement.dao;

import ra.Coursemanagement.model.Enrollment;
import java.util.List;

public interface IEnrollmentDAO {
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
    List<Enrollment> getEnrollmentsByCourseId(int courseId);
    List<Enrollment> getEnrollmentsWithDetails();
    Enrollment getEnrollmentById(int id);
    boolean addEnrollment(Enrollment enrollment);
    boolean updateEnrollmentStatus(int enrollmentId, String status);
    boolean deleteEnrollment(int id);
    boolean deleteEnrollment(int studentId, int courseId);
    boolean isStudentEnrolled(int studentId, int courseId);
    List<Enrollment> getStudentEnrollmentsWithCourseInfo(int studentId);
    int getStudentCountByCourseId(int courseId);
    List<Object[]> getStudentCountPerCourse();
}