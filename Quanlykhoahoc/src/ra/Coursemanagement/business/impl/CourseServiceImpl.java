package ra.Coursemanagement.business.impl;

import ra.Coursemanagement.business.ICourseService;
import ra.Coursemanagement.dao.ICourseDAO;
import ra.Coursemanagement.dao.IEnrollmentDAO;
import ra.Coursemanagement.dao.impl.CourseDAOImpl;
import ra.Coursemanagement.dao.impl.EnrollmentDAOImpl;
import ra.Coursemanagement.model.Course;

import java.time.LocalDate;
import java.util.List;

public class CourseServiceImpl implements ICourseService {

    private ICourseDAO courseDAO;
    private IEnrollmentDAO enrollmentDAO;

    public CourseServiceImpl() {
        this.courseDAO = new CourseDAOImpl();
        this.enrollmentDAO = new EnrollmentDAOImpl();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    @Override
    public Course getCourseById(int id) {
        return courseDAO.getCourseById(id);
    }

    @Override
    public Course getCourseByName(String name) {
        return courseDAO.getCourseByName(name);
    }

    @Override
    public boolean addCourse(Course course) {
        if (!validateCourseData(course)) {
            return false;
        }

        if (isCourseNameExists(course.getName())) {
            return false;
        }

        if (course.getId() <= 0) {
            course.setId(generateNewCourseId());
        }

        if (course.getCreateAt() == null) {
            course.setCreateAt(LocalDate.now());
        }

        return courseDAO.addCourse(course);
    }

    @Override
    public boolean updateCourse(Course course) {
        if (!validateCourseData(course)) {
            return false;
        }

        Course existingCourse = courseDAO.getCourseById(course.getId());
        if (existingCourse == null) {
            return false;
        }

        // Kiểm tra tên khóa học đã tồn tại chưa (nếu thay đổi tên)
        if (!existingCourse.getName().equalsIgnoreCase(course.getName())) {
            if (isCourseNameExists(course.getName())) {
                return false;
            }
        }

        return courseDAO.updateCourse(course);
    }

    @Override
    public boolean deleteCourse(int id) {
        // Kiểm tra xem khóa học có học viên đăng ký không
        int studentCount = enrollmentDAO.getStudentCountByCourseId(id);
        if (studentCount > 0) {
            return false; // Không thể xóa vì có học viên đã đăng ký
        }

        return courseDAO.deleteCourse(id);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseDAO.searchCourses(keyword.trim());
    }

    @Override
    public List<Course> sortCourses(String sortBy, boolean ascending) {
        return courseDAO.sortCourses(sortBy, ascending);
    }

    @Override
    public int getTotalCourses() {
        return courseDAO.getTotalCourses();
    }

    @Override
    public List<Course> getTopCourses(int limit) {
        return courseDAO.getTopCourses(limit);
    }

    @Override
    public List<Course> getCoursesWithMoreThan(int studentCount) {
        return courseDAO.getCoursesWithMoreThan(studentCount);
    }

    @Override
    public boolean isCourseNameExists(String name) {
        return courseDAO.getCourseByName(name) != null;
    }

    @Override
    public int generateNewCourseId() {
        List<Course> courses = courseDAO.getAllCourses();
        if (courses.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (Course c : courses) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public boolean validateCourseData(Course course) {
        if (course == null) {
            return false;
        }

        // Kiểm tra tên
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            return false;
        }

        // Kiểm tra thời lượng
        if (course.getDuration() <= 0) {
            return false;
        }

        // Kiểm tra giảng viên
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
            return false;
        }

        return true;
    }
}