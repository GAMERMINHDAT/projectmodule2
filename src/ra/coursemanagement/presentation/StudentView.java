package ra.coursemanagement.presentation;

import ra.coursemanagement.business.*;
import ra.coursemanagement.business.impl.*;
import ra.coursemanagement.model.*;
import ra.coursemanagement.utils.InputValidator;

import java.util.List;

public class StudentView {
    private Student currentStudent;
    private ICourseService courseService = new CourseServiceImpl();
    private IEnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private IStudentService studentService = new StudentServiceImpl();

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
    }

    public void showStudentMenu() {
        while (true) {
            System.out.println("\n======== MENU HỌC VIÊN =========");
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Đăng ký khóa học");
            System.out.println("3. Xem khóa học đã đăng ký");
            System.out.println("4. Hủy đăng ký");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("================================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    viewCourses();
                    break;
                case 2:
                    registerCourse();
                    break;
                case 3:
                    viewRegisteredCourses();
                    break;
                case 4:
                    cancelRegistration();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    System.out.println("Đăng xuất thành công!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewCourses() {
        System.out.println("\n=== DANH SÁCH KHÓA HỌC ===");
        System.out.println("1. Xem tất cả");
        System.out.println("2. Tìm kiếm theo tên");
        System.out.println("3. Quay lại");

        int choice = InputValidator.getIntInput("Chọn: ");

        switch (choice) {
            case 1:
                displayAllCourses();
                break;
            case 2:
                searchCourses();
                break;
            case 3:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private void displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào!");
            return;
        }

        System.out.println("\n=== DANH SÁCH KHÓA HỌC ===");
        System.out.printf("%-5s %-30s %-10s %-20s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên");
        System.out.println("--------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-5d %-30s %-10d %-20s%n",
                    course.getId(),
                    course.getName(),
                    course.getDuration(),
                    course.getInstructor());
        }
    }

    private void searchCourses() {
        String keyword = InputValidator.getStringInput("Nhập tên khóa học cần tìm: ");
        List<Course> courses = courseService.searchCoursesByName(keyword);

        if (courses.isEmpty()) {
            System.out.println("Không tìm thấy khóa học nào!");
            return;
        }

        System.out.println("\n=== KẾT QUẢ TÌM KIẾM ===");
        System.out.printf("%-5s %-30s %-10s %-20s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên");
        System.out.println("--------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-5d %-30s %-10d %-20s%n",
                    course.getId(),
                    course.getName(),
                    course.getDuration(),
                    course.getInstructor());
        }
    }

    private void registerCourse() {
        displayAllCourses();

        int courseId = InputValidator.getIntInput("\nNhập ID khóa học muốn đăng ký: ");
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return;
        }

        if (enrollmentService.registerCourse(currentStudent.getId(), courseId)) {
            System.out.println("Đăng ký thành công! Vui lòng chờ xác nhận.");
        }
    }

    private void viewRegisteredCourses() {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(currentStudent.getId());

        if (enrollments.isEmpty()) {
            System.out.println("Bạn chưa đăng ký khóa học nào!");
            return;
        }

        System.out.println("\n=== KHÓA HỌC ĐÃ ĐĂNG KÝ ===");
        System.out.printf("%-5s %-30s %-20s %-15s%n", "ID", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
        System.out.println("--------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            Course course = courseService.getCourseById(enrollment.getCourseId());
            if (course != null) {
                System.out.printf("%-5d %-30s %-20s %-15s%n",
                        enrollment.getId(),
                        course.getName(),
                        enrollment.getRegisteredAt().toLocalDate(),
                        enrollment.getStatus());
            }
        }

        // Tùy chọn sắp xếp
        System.out.println("\nBạn có muốn sắp xếp không?");
        System.out.println("1. Sắp xếp theo tên");
        System.out.println("2. Sắp xếp theo ngày đăng ký");
        System.out.println("3. Không sắp xếp");

        int choice = InputValidator.getIntInput("Chọn: ");

        if (choice == 1 || choice == 2) {
            System.out.println("Thứ tự:");
            System.out.println("1. Tăng dần");
            System.out.println("2. Giảm dần");

            int orderChoice = InputValidator.getIntInput("Chọn: ");
            String order = orderChoice == 1 ? "asc" : "desc";

            // Sắp xếp và hiển thị lại (có thể implement thêm)
            System.out.println("Tính năng đang phát triển!");
        }
    }

    private void cancelRegistration() {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(currentStudent.getId());
        List<Enrollment> cancellableEnrollments = enrollments.stream()
                .filter(e -> e.getStatus().equals("WAITING"))
                .toList();

        if (cancellableEnrollments.isEmpty()) {
            System.out.println("Không có đăng ký nào có thể hủy!");
            return;
        }

        System.out.println("\n=== CÁC ĐĂNG KÝ CÓ THỂ HỦY ===");
        for (Enrollment enrollment : cancellableEnrollments) {
            Course course = courseService.getCourseById(enrollment.getCourseId());
            if (course != null) {
                System.out.println("ID: " + enrollment.getId() + " - Khóa học: " + course.getName());
            }
        }

        int enrollmentId = InputValidator.getIntInput("\nNhập ID đăng ký muốn hủy: ");

        System.out.println("Bạn có chắc chắn muốn hủy đăng ký này?");
        String confirm = InputValidator.getStringInput("Nhập 'yes' để xác nhận: ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (enrollmentService.cancelEnrollment(enrollmentId)) {
                System.out.println("Hủy đăng ký thành công!");
            } else {
                System.out.println("Hủy đăng ký thất bại!");
            }
        }
    }

    private void changePassword() {
        System.out.println("\n=== ĐỔI MẬT KHẨU ===");

        String oldPassword = InputValidator.getStringInput("Mật khẩu cũ: ");
        String newPassword = InputValidator.getStringInput("Mật khẩu mới: ");
        String confirmPassword = InputValidator.getStringInput("Xác nhận mật khẩu mới: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Mật khẩu xác nhận không khớp!");
            return;
        }

        // Giả sử có xác thực qua email (có thể implement thêm)
        System.out.println("Vui lòng xác thực qua email...");
        String emailCode = InputValidator.getStringInput("Nhập mã xác thực (giả sử là 123456): ");

        if (!emailCode.equals("123456")) {
            System.out.println("Mã xác thực không đúng!");
            return;
        }

        if (studentService.changePassword(currentStudent.getId(), oldPassword, newPassword)) {
            System.out.println("Đổi mật khẩu thành công!");
        } else {
            System.out.println("Đổi mật khẩu thất bại!");
        }
    }
}