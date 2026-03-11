package ra.Coursemanagement.presentation;

import ra.Coursemanagement.business.*;
import ra.Coursemanagement.business.impl.*;
import ra.Coursemanagement.model.*;
import ra.Coursemanagement.utils.TableFormatter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StudentView {
    private Scanner scanner;
    private IStudentService studentService;
    private ICourseService courseService;
    private IEnrollmentService enrollmentService;
    private Student loggedInStudent;

    public StudentView() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentServiceImpl();
        this.courseService = new CourseServiceImpl();
        this.enrollmentService = new EnrollmentServiceImpl();
    }

    public void setLoggedInStudent(String email) {
        this.loggedInStudent = studentService.getStudentByEmail(email);
    }

    public void showMenu() {
        if (loggedInStudent == null) {
            System.out.println("Lỗi: Không tìm thấy thông tin học viên!");
            return;
        }

        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                 MENU HỌC VIÊN");
            System.out.println("=".repeat(60));
            System.out.println("Xin chào, " + loggedInStudent.getName() + "!");
            System.out.println("-".repeat(60));
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Đăng ký khóa học");
            System.out.println("3. Xem khóa học đã đăng ký");
            System.out.println("4. Hủy đăng ký (nếu chưa được xác nhận)");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllCourses();
                    break;
                case "2":
                    registerCourse();
                    break;
                case "3":
                    viewRegisteredCourses();
                    break;
                case "4":
                    cancelRegistration();
                    break;
                case "5":
                    changePassword();
                    break;
                case "6":
                    System.out.println("Đăng xuất thành công!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewAllCourses() {
        System.out.println("\n=== DANH SÁCH KHÓA HỌC ===");

        List<Course> courses = courseService.getAllCourses();
        TableFormatter.printCourseTable(courses);

        // Tìm kiếm khóa học
        System.out.println("\nBạn có muốn tìm kiếm khóa học không? (y/n): ");
        String searchChoice = scanner.nextLine().trim().toLowerCase();

        if (searchChoice.equals("y")) {
            System.out.print("Nhập tên khóa học cần tìm: ");
            String keyword = scanner.nextLine().trim();

            List<Course> results = courseService.searchCourses(keyword);
            if (results.isEmpty()) {
                System.out.println("Không tìm thấy khóa học nào với từ khóa: " + keyword);
            } else {
                System.out.println("\nKết quả tìm kiếm:");
                TableFormatter.printCourseTable(results);
            }
        }

        // Gợi ý khóa học (Bonus)
        suggestCourses();

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void suggestCourses() {
        // Lấy danh sách khóa học đã đăng ký của học viên
        List<Enrollment> registered = enrollmentService.getStudentEnrollmentsWithCourseInfo(loggedInStudent.getId());

        if (registered.isEmpty()) {
            return; // Không có gợi ý nếu chưa đăng ký khóa học nào
        }

        // Lấy danh sách ID khóa học đã đăng ký
        List<Integer> registeredCourseIds = registered.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toList());

        // Lấy tất cả khóa học
        List<Course> allCourses = courseService.getAllCourses();

        // Lọc ra các khóa học chưa đăng ký
        List<Course> availableCourses = allCourses.stream()
                .filter(c -> !registeredCourseIds.contains(c.getId()))
                .collect(Collectors.toList());

        if (availableCourses.isEmpty()) {
            return;
        }

        // Gợi ý đơn giản: dựa trên giảng viên của các khóa học đã đăng ký
        List<String> instructors = registered.stream()
                .map(e -> {
                    Course c = courseService.getCourseById(e.getCourseId());
                    return c != null ? c.getInstructor() : "";
                })
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        List<Course> suggestions = availableCourses.stream()
                .filter(c -> instructors.contains(c.getInstructor()))
                .limit(3)
                .collect(Collectors.toList());

        if (!suggestions.isEmpty()) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("🎯 GỢI Ý KHÓA HỌC DÀNH CHO BẠN:");
            for (Course c : suggestions) {
                System.out.printf("  • %s (Giảng viên: %s)%n", c.getName(), c.getInstructor());
            }
            System.out.println("-".repeat(60));
        }
    }

    private void registerCourse() {
        System.out.println("\n=== ĐĂNG KÝ KHÓA HỌC ===");

        // Hiển thị danh sách khóa học có thể đăng ký
        List<Course> allCourses = courseService.getAllCourses();
        List<Enrollment> registered = enrollmentService.getStudentEnrollmentsWithCourseInfo(loggedInStudent.getId());
        List<Integer> registeredIds = registered.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toList());

        List<Course> availableCourses = allCourses.stream()
                .filter(c -> !registeredIds.contains(c.getId()))
                .collect(Collectors.toList());

        if (availableCourses.isEmpty()) {
            System.out.println("Bạn đã đăng ký tất cả các khóa học hiện có!");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nCác khóa học bạn có thể đăng ký:");
        for (Course c : availableCourses) {
            System.out.printf("ID: %d - %s (Giảng viên: %s)%n",
                    c.getId(), c.getName(), c.getInstructor());
        }

        int courseId;
        while (true) {
            System.out.print("\nNhập ID khóa học muốn đăng ký: ");
            try {
                courseId = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Course selected = courseService.getCourseById(courseId);
        if (selected == null) {
            System.out.println("Không tìm thấy khóa học với ID: " + courseId);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (registeredIds.contains(courseId)) {
            System.out.println("Bạn đã đăng ký khóa học này rồi!");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nXác nhận đăng ký khóa học:");
        System.out.println("Khóa học: " + selected.getName());
        System.out.println("Giảng viên: " + selected.getInstructor());
        System.out.println("Thời lượng: " + selected.getDuration() + " giờ");

        System.out.print("\nXác nhận đăng ký? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            if (enrollmentService.registerCourse(loggedInStudent.getId(), courseId)) {
                System.out.println("✓ Đăng ký khóa học thành công! Vui lòng chờ admin xác nhận.");
            } else {
                System.out.println("✗ Đăng ký thất bại!");
            }
        } else {
            System.out.println("Hủy đăng ký.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void viewRegisteredCourses() {
        System.out.println("\n=== KHÓA HỌC ĐÃ ĐĂNG KÝ ===");

        List<Enrollment> enrollments = enrollmentService.getStudentEnrollmentsWithCourseInfo(loggedInStudent.getId());

        if (enrollments.isEmpty()) {
            System.out.println("Bạn chưa đăng ký khóa học nào.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n" + "-".repeat(80));
        System.out.printf("%-5s %-30s %-15s %-15s %-15s%n",
                "STT", "Tên khóa học", "Giảng viên", "Ngày đăng ký", "Trạng thái");
        System.out.println("-".repeat(80));

        int stt = 1;
        for (Enrollment e : enrollments) {
            Course course = courseService.getCourseById(e.getCourseId());
            if (course != null) {
                System.out.printf("%-5d %-30s %-15s %-15s %-15s%n",
                        stt++,
                        course.getName(),
                        course.getInstructor(),
                        e.getRegisteredAt().toLocalDate(),
                        e.getStatusInVietnamese());
            }
        }
        System.out.println("-".repeat(80));

        // Sắp xếp
        System.out.println("\nSắp xếp theo:");
        System.out.println("1. Tên khóa học (A-Z)");
        System.out.println("2. Tên khóa học (Z-A)");
        System.out.println("3. Ngày đăng ký (mới nhất)");
        System.out.println("4. Ngày đăng ký (cũ nhất)");
        System.out.println("5. Quay lại");
        System.out.print("Chọn: ");

        String sortChoice = scanner.nextLine().trim();

        List<Enrollment> sorted = enrollments;

        switch (sortChoice) {
            case "1":
                sorted = enrollments.stream()
                        .sorted(Comparator.comparing(e -> {
                            Course c = courseService.getCourseById(e.getCourseId());
                            return c != null ? c.getName() : "";
                        }))
                        .collect(Collectors.toList());
                break;
            case "2":
                sorted = enrollments.stream()
                        .sorted((e1, e2) -> {
                            String name1 = courseService.getCourseById(e1.getCourseId()) != null ?
                                    courseService.getCourseById(e1.getCourseId()).getName() : "";
                            String name2 = courseService.getCourseById(e2.getCourseId()) != null ?
                                    courseService.getCourseById(e2.getCourseId()).getName() : "";
                            return name2.compareTo(name1);
                        })
                        .collect(Collectors.toList());
                break;
            case "3":
                sorted = enrollments.stream()
                        .sorted((e1, e2) -> e2.getRegisteredAt().compareTo(e1.getRegisteredAt()))
                        .collect(Collectors.toList());
                break;
            case "4":
                sorted = enrollments.stream()
                        .sorted(Comparator.comparing(Enrollment::getRegisteredAt))
                        .collect(Collectors.toList());
                break;
            default:
                return;
        }

        if (!sortChoice.equals("5")) {
            System.out.println("\n=== DANH SÁCH ĐÃ SẮP XẾP ===");
            System.out.println("-".repeat(80));
            System.out.printf("%-5s %-30s %-15s %-15s %-15s%n",
                    "STT", "Tên khóa học", "Giảng viên", "Ngày đăng ký", "Trạng thái");
            System.out.println("-".repeat(80));

            stt = 1;
            for (Enrollment e : sorted) {
                Course course = courseService.getCourseById(e.getCourseId());
                if (course != null) {
                    System.out.printf("%-5d %-30s %-15s %-15s %-15s%n",
                            stt++,
                            course.getName(),
                            course.getInstructor(),
                            e.getRegisteredAt().toLocalDate(),
                            e.getStatusInVietnamese());
                }
            }
            System.out.println("-".repeat(80));
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void cancelRegistration() {
        System.out.println("\n=== HỦY ĐĂNG KÝ KHÓA HỌC ===");

        List<Enrollment> enrollments = enrollmentService.getStudentEnrollmentsWithCourseInfo(loggedInStudent.getId());
        List<Enrollment> cancellable = enrollments.stream()
                .filter(e -> "WAITING".equals(e.getStatus()))
                .collect(Collectors.toList());

        if (cancellable.isEmpty()) {
            System.out.println("Không có đăng ký nào có thể hủy (chỉ hủy được các đăng ký đang chờ duyệt).");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nCác đăng ký có thể hủy:");
        for (Enrollment e : cancellable) {
            Course course = courseService.getCourseById(e.getCourseId());
            if (course != null) {
                System.out.printf("ID ĐK: %d - %s (Đăng ký ngày: %s)%n",
                        e.getId(),
                        course.getName(),
                        e.getRegisteredAt().toLocalDate());
            }
        }

        int enrollmentId;
        while (true) {
            System.out.print("\nNhập ID đăng ký muốn hủy: ");
            try {
                enrollmentId = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        int finalEnrollmentId = enrollmentId;
        Enrollment selected = cancellable.stream()
                .filter(e -> e.getId() == finalEnrollmentId)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("Không tìm thấy đăng ký với ID: " + enrollmentId);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        Course course = courseService.getCourseById(selected.getCourseId());

        System.out.println("\nXác nhận hủy đăng ký:");
        System.out.println("Khóa học: " + (course != null ? course.getName() : "N/A"));
        System.out.println("Ngày đăng ký: " + selected.getRegisteredAt().toLocalDate());

        System.out.print("\nBạn có chắc chắn muốn hủy? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            if (enrollmentService.cancelEnrollment(enrollmentId)) {
                System.out.println("✓ Hủy đăng ký thành công!");
            } else {
                System.out.println("✗ Hủy đăng ký thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void changePassword() {
        System.out.println("\n=== ĐỔI MẬT KHẨU ===");

        System.out.print("Nhập mật khẩu cũ: ");
        String oldPassword = scanner.nextLine().trim();

        String newPassword;
        while (true) {
            System.out.print("Nhập mật khẩu mới (tối thiểu 6 ký tự): ");
            newPassword = scanner.nextLine().trim();
            if (newPassword.length() < 6) {
                System.out.println("Mật khẩu phải có ít nhất 6 ký tự!");
                continue;
            }
            break;
        }

        System.out.print("Xác nhận mật khẩu mới: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Mật khẩu xác nhận không khớp!");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (studentService.updatePassword(loggedInStudent.getId(), oldPassword, newPassword)) {
            System.out.println("✓ Đổi mật khẩu thành công!");
        } else {
            System.out.println("✗ Đổi mật khẩu thất bại! Vui lòng kiểm tra lại mật khẩu cũ.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }
}
