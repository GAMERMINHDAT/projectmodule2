package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.ICourseService;
import ra.coursemanagement.dao.ICourseDAO;
import ra.coursemanagement.dao.impl.CourseDAOImpl;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.utils.InputValidator;

import java.util.List;

public class CourseServiceImpl implements ICourseService {
    private ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public boolean addCourse(Course course) {
        // Validate dữ liệu
        if (!InputValidator.isValidName(course.getName())) {
            System.out.println("Tên khóa học không được để trống!");
            return false;
        }
        if (course.getDuration() <= 0) {
            System.out.println("Thời lượng khóa học phải lớn hơn 0!");
            return false;
        }
        if (!InputValidator.isValidName(course.getInstructor())) {
            System.out.println("Tên giảng viên không được để trống!");
            return false;
        }

        return courseDAO.add(course);
    }

    @Override
    public boolean updateCourse(Course course) {
        Course existingCourse = getCourseById(course.getId());
        if (existingCourse == null) {
            System.out.println("Không tìm thấy khóa học!");
            return false;
        }

        // Validate dữ liệu
        if (!InputValidator.isValidName(course.getName())) {
            System.out.println("Tên khóa học không được để trống!");
            return false;
        }
        if (course.getDuration() <= 0) {
            System.out.println("Thời lượng khóa học phải lớn hơn 0!");
            return false;
        }
        if (!InputValidator.isValidName(course.getInstructor())) {
            System.out.println("Tên giảng viên không được để trống!");
            return false;
        }

        return courseDAO.update(course);
    }

    @Override
    public boolean deleteCourse(int id) {
        Course course = getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return false;
        }
        return courseDAO.delete(id);
    }

    @Override
    public List<Course> searchCoursesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseDAO.searchByName(name);
    }

    @Override
    public List<Course> sortCourses(String field, String order) {
        if (!field.equalsIgnoreCase("name") && !field.equalsIgnoreCase("id")) {
            field = "id";
        }
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            order = "asc";
        }
        return courseDAO.sort(field, order);
    }

    @Override
    public int getTotalCourses() {
        return courseDAO.getTotalCourses();
    }

    @Override
    public int getNextId() {
        List<Course> courses = getAllCourses();
        if (courses.isEmpty()) {
            return 1;
        }
        return courses.stream().mapToInt(Course::getId).max().getAsInt() + 1;
    }
}