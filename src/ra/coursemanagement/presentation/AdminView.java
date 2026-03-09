package ra.coursemanagement.presentation;

import ra.coursemanagement.business.*;
import ra.coursemanagement.business.impl.*;
import ra.coursemanagement.model.*;
import ra.coursemanagement.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AdminView {
    private ICourseService courseService = new CourseServiceImpl();
    private IStudentService studentService = new StudentServiceImpl();
    private IEnrollmentService enrollmentService = new EnrollmentServiceImpl();

    public void showAdminMenu() {
        while (true) {
            System.out.println("\n======== MENU ADMIN =========");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê học viên theo khóa học");
            System.out.println("5. Đăng xuất");
            System.out.println("==============================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    showCourseManagementMenu();
                    break;
                case 2:
                    showStudentManagementMenu();
                    break;
                case 3:
                    showEnrollmentManagementMenu();
                    break;
                case 4:
                    showStatisticsMenu();
                    break;
                case 5:
                    System.out.println("Đăng xuất thành công!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void showCourseManagementMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ KHÓA HỌC ===");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học");
            System.out.println("5. Tìm kiếm theo tên");
            System.out.println("6. Sắp xếp theo tên hoặc id");
            System.out.println("7. Quay về menu chính");
            System.out.println("==========================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    displayAllCourses();
                    break;
                case 2:
                    addNewCourse();
                    break;
                case 3:
                    editCourse();
                    break;
                case 4:
                    deleteCourse();
                    break;
                case 5:
                    searchCourses();
                    break;
                case 6:
                    sortCourses();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào!");
            return;
        }

        System.out.println("\n=== DANH SÁCH KHÓA HỌC ===");
        System.out.printf("%-5s %-30s %-10s %-20s %-15s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên", "Ngày tạo");
        System.out.println("--------------------------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-5d %-30s %-10d %-20s %-15s%n",
                    course.getId(),
                    course.getName(),
                    course.getDuration(),
                    course.getInstructor(),
                    course.getCreateAt());
        }
    }

    private void addNewCourse() {
        System.out.println("\n=== THÊM MỚI KHÓA HỌC ===");
        Course course = new Course();

        course.setId(courseService.getNextId());
        course.setName(InputValidator.getStringInput("Tên khóa học: "));
        course.setDuration(InputValidator.getIntInput("Thời lượng (giờ): "));
        course.setInstructor(InputValidator.getStringInput("Giảng viên: "));
        course.setCreateAt(LocalDate.now());

        if (courseService.addCourse(course)) {
            System.out.println("Thêm khóa học thành công!");
        } else {
            System.out.println("Thêm khóa học thất bại!");
        }
    }

    private void editCourse() {
        int id = InputValidator.getIntInput("Nhập ID khóa học cần sửa: ");
        Course course = courseService.getCourseById(id);

        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return;
        }

        System.out.println("\n=== CHỈNH SỬA KHÓA HỌC ===");
        System.out.println("1. Sửa tên khóa học");
        System.out.println("2. Sửa thời lượng");
        System.out.println("3. Sửa giảng viên");
        System.out.println("4. Sửa tất cả");
        System.out.println("5. Quay lại");

        int choice = InputValidator.getIntInput("Chọn thuộc tính cần sửa: ");

        switch (choice) {
            case 1:
                course.setName(InputValidator.getStringInput("Tên mới: "));
                break;
            case 2:
                course.setDuration(InputValidator.getIntInput("Thời lượng mới: "));
                break;
            case 3:
                course.setInstructor(InputValidator.getStringInput("Giảng viên mới: "));
                break;
            case 4:
                course.setName(InputValidator.getStringInput("Tên mới: "));
                course.setDuration(InputValidator.getIntInput("Thời lượng mới: "));
                course.setInstructor(InputValidator.getStringInput("Giảng viên mới: "));
                break;
            case 5:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        if (courseService.updateCourse(course)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }

    private void deleteCourse() {
        int id = InputValidator.getIntInput("Nhập ID khóa học cần xóa: ");
        Course course = courseService.getCourseById(id);

        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa khóa học: " + course.getName() + "?");
        String confirm = InputValidator.getStringInput("Nhập 'yes' để xác nhận: ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (courseService.deleteCourse(id)) {
                System.out.println("Xóa thành công!");
            } else {
                System.out.println("Xóa thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa!");
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

    private void sortCourses() {
        System.out.println("Sắp xếp theo:");
        System.out.println("1. ID");
        System.out.println("2. Tên");

        int fieldChoice = InputValidator.getIntInput("Chọn: ");
        String field = fieldChoice == 1 ? "id" : "name";

        System.out.println("Thứ tự:");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");

        int orderChoice = InputValidator.getIntInput("Chọn: ");
        String order = orderChoice == 1 ? "asc" : "desc";

        List<Course> courses = courseService.sortCourses(field, order);

        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào!");
            return;
        }

        System.out.println("\n=== DANH SÁCH KHÓA HỌC ĐÃ SẮP XẾP ===");
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

    private void showStudentManagementMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ HỌC VIÊN ===");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên");
            System.out.println("4. Xóa học viên");
            System.out.println("5. Tìm kiếm học viên");
            System.out.println("6. Sắp xếp học viên");
            System.out.println("7. Quay về menu chính");
            System.out.println("=========================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    displayAllStudents();
                    break;
                case 2:
                    addNewStudent();
                    break;
                case 3:
                    editStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    searchStudents();
                    break;
                case 6:
                    sortStudents();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("Không có học viên nào!");
            return;
        }

        System.out.println("\n=== DANH SÁCH HỌC VIÊN ===");
        System.out.printf("%-5s %-20s %-12s %-25s %-5s %-15s %-12s%n",
                "ID", "Họ tên", "Ngày sinh", "Email", "GT", "SĐT", "Ngày tạo");
        System.out.println("--------------------------------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-5d %-20s %-12s %-25s %-5s %-15s %-12s%n",
                    student.getId(),
                    student.getName(),
                    student.getDob(),
                    student.getEmail(),
                    student.getSexString(),
                    student.getPhone(),
                    student.getCreateAt());
        }
    }

    private void addNewStudent() {
        System.out.println("\n=== THÊM MỚI HỌC VIÊN ===");
        Student student = new Student();

        student.setId(studentService.getNextId());
        student.setName(InputValidator.getStringInput("Họ tên: "));
        student.setDob(InputValidator.getDateInput("Ngày sinh (yyyy-MM-dd): "));
        student.setEmail(InputValidator.getStringInput("Email: "));

        System.out.println("Giới tính (0: Nữ, 1: Nam): ");
        student.setSex(InputValidator.getIntInput("Chọn: "));

        student.setPhone(InputValidator.getStringInput("Số điện thoại: "));
        student.setPassword(InputValidator.getStringInput("Mật khẩu: "));
        student.setCreateAt(LocalDate.now());

        if (studentService.addStudent(student)) {
            System.out.println("Thêm học viên thành công!");
        } else {
            System.out.println("Thêm học viên thất bại!");
        }
    }

    private void editStudent() {
        int id = InputValidator.getIntInput("Nhập ID học viên cần sửa: ");
        Student student = studentService.getStudentById(id);

        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return;
        }

        System.out.println("\n=== CHỈNH SỬA HỌC VIÊN ===");
        System.out.println("1. Sửa họ tên");
        System.out.println("2. Sửa ngày sinh");
        System.out.println("3. Sửa email");
        System.out.println("4. Sửa giới tính");
        System.out.println("5. Sửa số điện thoại");
        System.out.println("6. Sửa mật khẩu");
        System.out.println("7. Sửa tất cả");
        System.out.println("8. Quay lại");

        int choice = InputValidator.getIntInput("Chọn thuộc tính cần sửa: ");

        switch (choice) {
            case 1:
                student.setName(InputValidator.getStringInput("Họ tên mới: "));
                break;
            case 2:
                student.setDob(InputValidator.getDateInput("Ngày sinh mới (yyyy-MM-dd): "));
                break;
            case 3:
                student.setEmail(InputValidator.getStringInput("Email mới: "));
                break;
            case 4:
                System.out.println("Giới tính (0: Nữ, 1: Nam): ");
                student.setSex(InputValidator.getIntInput("Chọn: "));
                break;
            case 5:
                student.setPhone(InputValidator.getStringInput("Số điện thoại mới: "));
                break;
            case 6:
                student.setPassword(InputValidator.getStringInput("Mật khẩu mới: "));
                break;
            case 7:
                student.setName(InputValidator.getStringInput("Họ tên mới: "));
                student.setDob(InputValidator.getDateInput("Ngày sinh mới (yyyy-MM-dd): "));
                student.setEmail(InputValidator.getStringInput("Email mới: "));
                System.out.println("Giới tính (0: Nữ, 1: Nam): ");
                student.setSex(InputValidator.getIntInput("Chọn: "));
                student.setPhone(InputValidator.getStringInput("Số điện thoại mới: "));
                student.setPassword(InputValidator.getStringInput("Mật khẩu mới: "));
                break;
            case 8:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        if (studentService.updateStudent(student)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }

    private void deleteStudent() {
        int id = InputValidator.getIntInput("Nhập ID học viên cần xóa: ");
        Student student = studentService.getStudentById(id);

        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa học viên: " + student.getName() + "?");
        String confirm = InputValidator.getStringInput("Nhập 'yes' để xác nhận: ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (studentService.deleteStudent(id)) {
                System.out.println("Xóa thành công!");
            } else {
                System.out.println("Xóa thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa!");
        }
    }

    private void searchStudents() {
        String keyword = InputValidator.getStringInput("Nhập từ khóa tìm kiếm (tên, email hoặc ID): ");
        List<Student> students = studentService.searchStudents(keyword);

        if (students.isEmpty()) {
            System.out.println("Không tìm thấy học viên nào!");
            return;
        }

        System.out.println("\n=== KẾT QUẢ TÌM KIẾM ===");
        System.out.printf("%-5s %-20s %-25s %-5s %-15s%n", "ID", "Họ tên", "Email", "GT", "SĐT");
        System.out.println("--------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-5d %-20s %-25s %-5s %-15s%n",
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getSexString(),
                    student.getPhone());
        }
    }

    private void sortStudents() {
        System.out.println("Sắp xếp theo:");
        System.out.println("1. ID");
        System.out.println("2. Tên");

        int fieldChoice = InputValidator.getIntInput("Chọn: ");
        String field = fieldChoice == 1 ? "id" : "name";

        System.out.println("Thứ tự:");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");

        int orderChoice = InputValidator.getIntInput("Chọn: ");
        String order = orderChoice == 1 ? "asc" : "desc";

        List<Student> students = studentService.sortStudents(field, order);

        if (students.isEmpty()) {
            System.out.println("Không có học viên nào!");
            return;
        }

        System.out.println("\n=== DANH SÁCH HỌC VIÊN ĐÃ SẮP XẾP ===");
        System.out.printf("%-5s %-20s %-25s %-5s %-15s%n", "ID", "Họ tên", "Email", "GT", "SĐT");
        System.out.println("--------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-5d %-20s %-25s %-5s %-15s%n",
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getSexString(),
                    student.getPhone());
        }
    }

    private void showEnrollmentManagementMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐĂNG KÝ KHÓA HỌC ===");
            System.out.println("1. Hiển thị học viên theo từng khóa học");
            System.out.println("2. Thêm học viên vào khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học");
            System.out.println("4. Quay về menu chính");
            System.out.println("==================================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    displayStudentsByCourse();
                    break;
                case 2:
                    addStudentToCourse();
                    break;
                case 3:
                    removeStudentFromCourse();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayStudentsByCourse() {
        int courseId = InputValidator.getIntInput("Nhập ID khóa học: ");
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return;
        }

        List<Student> students = enrollmentService.getStudentsByCourse(courseId);

        System.out.println("\n=== HỌC VIÊN ĐĂNG KÝ KHÓA HỌC: " + course.getName() + " ===");
        if (students.isEmpty()) {
            System.out.println("Chưa có học viên nào đăng ký!");
            return;
        }

        System.out.printf("%-5s %-20s %-25s %-5s %-15s%n", "ID", "Họ tên", "Email", "GT", "SĐT");
        System.out.println("--------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-5d %-20s %-25s %-5s %-15s%n",
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getSexString(),
                    student.getPhone());
        }
    }

    private void addStudentToCourse() {
        int studentId = InputValidator.getIntInput("Nhập ID học viên: ");
        Student student = studentService.getStudentById(studentId);

        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return;
        }

        int courseId = InputValidator.getIntInput("Nhập ID khóa học: ");
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("Không tìm thấy khóa học!");
            return;
        }

        if (enrollmentService.registerCourse(studentId, courseId)) {
            System.out.println("Đăng ký thành công!");
        }
    }

    private void removeStudentFromCourse() {
        int studentId = InputValidator.getIntInput("Nhập ID học viên: ");
        int courseId = InputValidator.getIntInput("Nhập ID khóa học: ");

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getCourseId() == courseId)
                .findFirst()
                .orElse(null);

        if (enrollment == null) {
            System.out.println("Không tìm thấy đăng ký!");
            return;
        }

        System.out.println("Bạn có chắc chắn muốn xóa học viên khỏi khóa học?");
        String confirm = InputValidator.getStringInput("Nhập 'yes' để xác nhận: ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (enrollmentService.cancelEnrollment(enrollment.getId())) {
                System.out.println("Xóa thành công!");
            } else {
                System.out.println("Xóa thất bại!");
            }
        }
    }

    private void showStatisticsMenu() {
        while (true) {
            System.out.println("\n=== THỐNG KÊ ===");
            System.out.println("1. Thống kê tổng số lượng khóa học và học viên");
            System.out.println("2. Thống kê học viên theo từng khóa học");
            System.out.println("3. Top 5 khóa học đông học viên nhất");
            System.out.println("4. Liệt kê khóa học có trên 10 học viên");
            System.out.println("5. Quay về menu chính");
            System.out.println("================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    displayTotalStatistics();
                    break;
                case 2:
                    displayStudentCountByCourse();
                    break;
                case 3:
                    displayTopCourses();
                    break;
                case 4:
                    displayCoursesWithMoreThan10Students();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayTotalStatistics() {
        int totalCourses = courseService.getTotalCourses();
        int totalStudents = studentService.getAllStudents().size();

        System.out.println("\n=== THỐNG KÊ TỔNG QUAN ===");
        System.out.println("Tổng số khóa học: " + totalCourses);
        System.out.println("Tổng số học viên: " + totalStudents);
    }

    private void displayStudentCountByCourse() {
        Map<Course, Long> stats = enrollmentService.getStudentCountByCourse();

        if (stats.isEmpty()) {
            System.out.println("Không có dữ liệu!");
            return;
        }

        System.out.println("\n=== THỐNG KÊ HỌC VIÊN THEO KHÓA HỌC ===");
        System.out.printf("%-30s %-20s %-15s%n", "Tên khóa học", "Giảng viên", "Số học viên");
        System.out.println("--------------------------------------------------");

        for (Map.Entry<Course, Long> entry : stats.entrySet()) {
            Course course = entry.getKey();
            System.out.printf("%-30s %-20s %-15d%n",
                    course.getName(),
                    course.getInstructor(),
                    entry.getValue());
        }
    }

    private void displayTopCourses() {
        List<Course> topCourses = enrollmentService.getTopCourses(5);

        if (topCourses.isEmpty()) {
            System.out.println("Không có dữ liệu!");
            return;
        }

        System.out.println("\n=== TOP 5 KHÓA HỌC ĐÔNG HỌC VIÊN NHẤT ===");
        System.out.printf("%-5s %-30s %-20s%n", "STT", "Tên khóa học", "Giảng viên");
        System.out.println("----------------------------------------");

        int index = 1;
        for (Course course : topCourses) {
            System.out.printf("%-5d %-30s %-20s%n",
                    index++,
                    course.getName(),
                    course.getInstructor());
        }
    }

    private void displayCoursesWithMoreThan10Students() {
        List<Course> courses = enrollmentService.getCoursesWithMoreThan10Students();

        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào có trên 10 học viên!");
            return;
        }

        System.out.println("\n=== KHÓA HỌC CÓ TRÊN 10 HỌC VIÊN ===");
        System.out.printf("%-30s %-20s%n", "Tên khóa học", "Giảng viên");
        System.out.println("----------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-30s %-20s%n",
                    course.getName(),
                    course.getInstructor());
        }
    }
}