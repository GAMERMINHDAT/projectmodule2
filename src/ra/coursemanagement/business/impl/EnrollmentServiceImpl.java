package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.IEnrollmentService;
import ra.coursemanagement.business.ICourseService;
import ra.coursemanagement.business.IStudentService;
import ra.coursemanagement.dao.IEnrollmentDAO;
import ra.coursemanagement.dao.impl.EnrollmentDAOImpl;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Enrollment;
import ra.coursemanagement.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrollmentServiceImpl implements IEnrollmentService {

    // Khai báo đúng các dependency
    private IEnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
    private IStudentService studentService = new StudentServiceImpl();
    private ICourseService courseService = new CourseServiceImpl();

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        List<Enrollment> enrollments = enrollmentDAO.findByCourseId(courseId);
        List<Student> students = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            Student student = studentService.getStudentById(enrollment.getStudentId());
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    @Override
    public boolean registerCourse(int studentId, int courseId) {
        // Kiểm tra học viên và khóa học tồn tại
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return false;
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return false;
        }

        // Kiểm tra đã đăng ký chưa
        if (enrollmentDAO.exists(studentId, courseId)) {
            System.out.println("Học viên đã đăng ký khóa học này!");
            return false;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(getNextId());
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setRegisteredAt(LocalDateTime.now());
        enrollment.setStatus("WAITING");

        return enrollmentDAO.add(enrollment);
    }

    @Override
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        if (!status.equals("WAITING") && !status.equals("DENIED") &&
                !status.equals("CANCEL") && !status.equals("CONFIRMED")) {
            System.out.println("Trạng thái không hợp lệ!");
            return false;
        }
        return enrollmentDAO.updateStatus(enrollmentId, status);
    }

    @Override
    public boolean cancelEnrollment(int enrollmentId) {
        List<Enrollment> enrollments = getAllEnrollments();
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getId() == enrollmentId)
                .findFirst()
                .orElse(null);

        if (enrollment == null) {
            System.out.println("Không tìm thấy đăng ký!");
            return false;
        }

        // Chỉ được hủy nếu chưa được xác nhận
        if (!enrollment.getStatus().equals("WAITING")) {
            System.out.println("Chỉ có thể hủy đăng ký khi chưa được xác nhận!");
            return false;
        }

        return enrollmentDAO.delete(enrollmentId);
    }

    @Override
    public Map<Course, Long> getStudentCountByCourse() {
        return enrollmentDAO.getStudentCountByCourse(); // Dòng này đã được sửa
    }

    @Override
    public List<Course> getTopCourses(int limit) {
        return enrollmentDAO.getTopCourses(limit); // Dòng này đã được sửa
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        return enrollmentDAO.getCoursesWithMoreThan10Students(); // Dòng này đã được sửa
    }

    @Override
    public int getNextId() {
        List<Enrollment> enrollments = getAllEnrollments();
        if (enrollments.isEmpty()) {
            return 1;
        }
        return enrollments.stream().mapToInt(Enrollment::getId).max().getAsInt() + 1;
    }
}