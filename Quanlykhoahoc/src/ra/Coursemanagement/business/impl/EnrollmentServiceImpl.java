package ra.Coursemanagement.business.impl;

import ra.Coursemanagement.business.IEnrollmentService;
import ra.Coursemanagement.dao.ICourseDAO;
import ra.Coursemanagement.dao.IEnrollmentDAO;
import ra.Coursemanagement.dao.IStudentDAO;
import ra.Coursemanagement.dao.impl.CourseDAOImpl;
import ra.Coursemanagement.dao.impl.EnrollmentDAOImpl;
import ra.Coursemanagement.dao.impl.StudentDAOImpl;
import ra.Coursemanagement.model.Course;
import ra.Coursemanagement.model.Enrollment;

import java.time.LocalDateTime;
import java.util.List;

public class EnrollmentServiceImpl implements IEnrollmentService {

    private IEnrollmentDAO enrollmentDAO;
    private IStudentDAO studentDAO;
    private ICourseDAO courseDAO;

    public EnrollmentServiceImpl() {
        this.enrollmentDAO = new EnrollmentDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.courseDAO = new CourseDAOImpl();
    }

    @Override
    public List<Enrollment> getAllEnrollmentsWithDetails() {
        return enrollmentDAO.getEnrollmentsWithDetails();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        return enrollmentDAO.getEnrollmentsByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        return enrollmentDAO.getEnrollmentsByCourseId(courseId);
    }

    @Override
    public List<Enrollment> getStudentEnrollmentsWithCourseInfo(int studentId) {
        return enrollmentDAO.getStudentEnrollmentsWithCourseInfo(studentId);
    }

    @Override
    public boolean registerCourse(int studentId, int courseId) {
        // Kiểm tra sinh viên tồn tại
        if (studentDAO.getStudentById(studentId) == null) {
            return false;
        }

        // Kiểm tra khóa học tồn tại
        if (courseDAO.getCourseById(courseId) == null) {
            return false;
        }

        // Kiểm tra đã đăng ký chưa
        if (enrollmentDAO.isStudentEnrolled(studentId, courseId)) {
            return false;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(generateNewEnrollmentId());
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setRegisteredAt(LocalDateTime.now());
        enrollment.setStatus("WAITING");

        return enrollmentDAO.addEnrollment(enrollment);
    }

    @Override
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        if (!isValidStatus(status)) {
            return false;
        }

        Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        return enrollmentDAO.updateEnrollmentStatus(enrollmentId, status);
    }

    @Override
    public boolean cancelEnrollment(int enrollmentId) {
        Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        // Chỉ có thể hủy nếu trạng thái là WAITING
        if (!"WAITING".equals(enrollment.getStatus())) {
            return false;
        }

        return enrollmentDAO.deleteEnrollment(enrollmentId);
    }

    @Override
    public boolean cancelEnrollment(int studentId, int courseId) {
        Enrollment enrollment = findEnrollment(studentId, courseId);
        if (enrollment == null) {
            return false;
        }

        // Chỉ có thể hủy nếu trạng thái là WAITING
        if (!"WAITING".equals(enrollment.getStatus())) {
            return false;
        }

        return enrollmentDAO.deleteEnrollment(studentId, courseId);
    }

    @Override
    public boolean deleteEnrollment(int id) {
        return enrollmentDAO.deleteEnrollment(id);
    }

    @Override
    public boolean isStudentEnrolled(int studentId, int courseId) {
        return enrollmentDAO.isStudentEnrolled(studentId, courseId);
    }

    @Override
    public int getStudentCountByCourseId(int courseId) {
        return enrollmentDAO.getStudentCountByCourseId(courseId);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return enrollmentDAO.getStudentCountPerCourse();
    }

    @Override
    public int getTotalConfirmedStudents() {
        List<Object[]> stats = enrollmentDAO.getStudentCountPerCourse();
        int total = 0;
        for (Object[] row : stats) {
            total += (Integer) row[2];
        }
        return total;
    }

    @Override
    public int generateNewEnrollmentId() {
        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        if (enrollments.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (Enrollment e : enrollments) {
            if (e.getId() > maxId) {
                maxId = e.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public int getTotalCourses() {
        return courseDAO.getTotalCourses();
    }

    @Override
    public int getTotalStudents() {
        return studentDAO.getTotalStudents();
    }

    @Override
    public List<Object[]> getStudentCountPerCourseForStats() {
        return enrollmentDAO.getStudentCountPerCourse();
    }

    @Override
    public List<Course> getTop5Courses() {
        return courseDAO.getTopCourses(5);
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        return courseDAO.getCoursesWithMoreThan(10);
    }

    private Enrollment findEnrollment(int studentId, int courseId) {
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudentId(studentId);
        for (Enrollment e : enrollments) {
            if (e.getCourseId() == courseId) {
                return e;
            }
        }
        return null;
    }

    private boolean isValidStatus(String status) {
        return status != null && (
                "WAITING".equals(status) ||
                        "CONFIRMED".equals(status) ||
                        "DENIED".equals(status) ||
                        "CANCEL".equals(status)
        );
    }
}