package ra.Coursemanagement.presentation;

import ra.Coursemanagement.business.*;
import ra.Coursemanagement.business.impl.*;
import ra.Coursemanagement.model.*;
import ra.Coursemanagement.utils.TableFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    private Scanner scanner;
    private ICourseService courseService;
    private IStudentService studentService;
    private IEnrollmentService enrollmentService;

    public AdminView() {
        this.scanner = new Scanner(System.in);
        this.courseService = new CourseServiceImpl();
        this.studentService = new StudentServiceImpl();
        this.enrollmentService = new EnrollmentServiceImpl();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    MENU ADMIN");
            System.out.println("=".repeat(60));
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê");
            System.out.println("5. Đăng xuất");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showCourseManagementMenu();
                    break;
                case "2":
                    showStudentManagementMenu();
                    break;
                case "3":
                    showEnrollmentManagementMenu();
                    break;
                case "4":
                    showStatisticsMenu();
                    break;
                case "5":
                    System.out.println("Đăng xuất thành công!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ==================== QUẢN LÝ KHÓA HỌC ====================
    private void showCourseManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                QUẢN LÝ KHÓA HỌC");
            System.out.println("=".repeat(60));
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học");
            System.out.println("5. Tìm kiếm khóa học");
            System.out.println("6. Sắp xếp khóa học");
            System.out.println("7. Quay lại menu chính");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayAllCourses();
                    break;
                case "2":
                    addCourse();
                    break;
                case "3":
                    editCourse();
                    break;
                case "4":
                    deleteCourse();
                    break;
                case "5":
                    searchCourses();
                    break;
                case "6":
                    sortCourses();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllCourses() {
        System.out.println("\n=== DANH SÁCH KHÓA HỌC ===");
        List<Course> courses = courseService.getAllCourses();
        TableFormatter.printCourseTable(courses);

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void addCourse() {
        System.out.println("\n=== THÊM MỚI KHÓA HỌC ===");

        Course course = new Course();

        // Nhập tên khóa học
        while (true) {
            System.out.print("Tên khóa học: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Tên khóa học không được để trống!");
                continue;
            }
            if (courseService.isCourseNameExists(name)) {
                System.out.println("Tên khóa học đã tồn tại!");
                continue;
            }
            course.setName(name);
            break;
        }

        // Nhập thời lượng
        while (true) {
            System.out.print("Thời lượng (giờ): ");
            try {
                int duration = Integer.parseInt(scanner.nextLine().trim());
                if (duration <= 0) {
                    System.out.println("Thời lượng phải lớn hơn 0!");
                    continue;
                }
                course.setDuration(duration);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
            }
        }

        // Nhập giảng viên
        while (true) {
            System.out.print("Giảng viên: ");
            String instructor = scanner.nextLine().trim();
            if (instructor.isEmpty()) {
                System.out.println("Tên giảng viên không được để trống!");
                continue;
            }
            course.setInstructor(instructor);
            break;
        }

        course.setId(courseService.generateNewCourseId());
        course.setCreateAt(LocalDate.now());

        if (courseService.addCourse(course)) {
            System.out.println("✓ Thêm khóa học thành công! Mã khóa học: " + course.getId());
        } else {
            System.out.println("✗ Thêm khóa học thất bại!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void editCourse() {
        System.out.println("\n=== CHỈNH SỬA KHÓA HỌC ===");

        int id;
        while (true) {
            System.out.print("Nhập ID khóa học cần sửa: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học với ID: " + id);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nThông tin hiện tại:");
        System.out.println("Tên: " + course.getName());
        System.out.println("Thời lượng: " + course.getDuration() + " giờ");
        System.out.println("Giảng viên: " + course.getInstructor());

        System.out.println("\n=== MENU CHỈNH SỬA ===");
        System.out.println("1. Sửa tên khóa học");
        System.out.println("2. Sửa thời lượng");
        System.out.println("3. Sửa giảng viên");
        System.out.println("4. Sửa tất cả");
        System.out.println("5. Hủy");
        System.out.print("Chọn thuộc tính cần sửa: ");

        String choice = scanner.nextLine().trim();

        boolean updated = false;

        switch (choice) {
            case "1":
                System.out.print("Tên mới: ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty() && !newName.equals(course.getName())) {
                    if (!courseService.isCourseNameExists(newName)) {
                        course.setName(newName);
                        updated = true;
                    } else {
                        System.out.println("Tên khóa học đã tồn tại!");
                    }
                }
                break;

            case "2":
                System.out.print("Thời lượng mới: ");
                try {
                    int newDuration = Integer.parseInt(scanner.nextLine().trim());
                    if (newDuration > 0) {
                        course.setDuration(newDuration);
                        updated = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Thời lượng không hợp lệ!");
                }
                break;

            case "3":
                System.out.print("Giảng viên mới: ");
                String newInstructor = scanner.nextLine().trim();
                if (!newInstructor.isEmpty()) {
                    course.setInstructor(newInstructor);
                    updated = true;
                }
                break;

            case "4":
                // Sửa tên
                while (true) {
                    System.out.print("Tên mới: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Tên không được để trống!");
                        continue;
                    }
                    if (!name.equals(course.getName()) && courseService.isCourseNameExists(name)) {
                        System.out.println("Tên khóa học đã tồn tại!");
                        continue;
                    }
                    course.setName(name);
                    break;
                }

                // Sửa thời lượng
                while (true) {
                    System.out.print("Thời lượng mới: ");
                    try {
                        int duration = Integer.parseInt(scanner.nextLine().trim());
                        if (duration <= 0) {
                            System.out.println("Thời lượng phải lớn hơn 0!");
                            continue;
                        }
                        course.setDuration(duration);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số hợp lệ!");
                    }
                }

                // Sửa giảng viên
                while (true) {
                    System.out.print("Giảng viên mới: ");
                    String instructor = scanner.nextLine().trim();
                    if (instructor.isEmpty()) {
                        System.out.println("Tên giảng viên không được để trống!");
                        continue;
                    }
                    course.setInstructor(instructor);
                    break;
                }

                updated = true;
                break;

            case "5":
                System.out.println("Hủy chỉnh sửa.");
                return;

            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        if (updated && courseService.updateCourse(course)) {
            System.out.println("✓ Cập nhật khóa học thành công!");
        } else if (updated) {
            System.out.println("✗ Cập nhật thất bại!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void deleteCourse() {
        System.out.println("\n=== XÓA KHÓA HỌC ===");

        int id;
        while (true) {
            System.out.print("Nhập ID khóa học cần xóa: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học với ID: " + id);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nThông tin khóa học:");
        System.out.println("ID: " + course.getId());
        System.out.println("Tên: " + course.getName());
        System.out.println("Giảng viên: " + course.getInstructor());

        // Kiểm tra số lượng học viên đã đăng ký
        int studentCount = enrollmentService.getStudentCountByCourseId(id);
        if (studentCount > 0) {
            System.out.println("\n⚠ Khóa học này có " + studentCount + " học viên đã đăng ký!");
            System.out.println("Không thể xóa khóa học đã có học viên đăng ký.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.print("\nBạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            if (courseService.deleteCourse(id)) {
                System.out.println("✓ Xóa khóa học thành công!");
            } else {
                System.out.println("✗ Xóa khóa học thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void searchCourses() {
        System.out.println("\n=== TÌM KIẾM KHÓA HỌC ===");
        System.out.print("Nhập từ khóa tìm kiếm: ");
        String keyword = scanner.nextLine().trim();

        List<Course> results = courseService.searchCourses(keyword);

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy khóa học nào với từ khóa: " + keyword);
        } else {
            System.out.println("\nKết quả tìm kiếm (" + results.size() + " khóa học):");
            TableFormatter.printCourseTable(results);
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void sortCourses() {
        System.out.println("\n=== SẮP XẾP KHÓA HỌC ===");
        System.out.println("Sắp xếp theo:");
        System.out.println("1. Tên");
        System.out.println("2. ID");
        System.out.print("Chọn: ");

        String sortChoice = scanner.nextLine().trim();
        String sortBy = sortChoice.equals("1") ? "name" : "id";

        System.out.println("\nThứ tự:");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Chọn: ");

        String orderChoice = scanner.nextLine().trim();
        boolean ascending = orderChoice.equals("1");

        List<Course> sorted = courseService.sortCourses(sortBy, ascending);

        System.out.println("\nDanh sách đã sắp xếp:");
        TableFormatter.printCourseTable(sorted);

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    // ==================== QUẢN LÝ HỌC VIÊN ====================
    private void showStudentManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                QUẢN LÝ HỌC VIÊN");
            System.out.println("=".repeat(60));
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên");
            System.out.println("4. Xóa học viên");
            System.out.println("5. Tìm kiếm học viên");
            System.out.println("6. Sắp xếp học viên");
            System.out.println("7. Quay lại menu chính");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayAllStudents();
                    break;
                case "2":
                    addStudent();
                    break;
                case "3":
                    editStudent();
                    break;
                case "4":
                    deleteStudent();
                    break;
                case "5":
                    searchStudents();
                    break;
                case "6":
                    sortStudents();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllStudents() {
        System.out.println("\n=== DANH SÁCH HỌC VIÊN ===");
        List<Student> students = studentService.getAllStudents();
        TableFormatter.printStudentTable(students);

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void addStudent() {
        System.out.println("\n=== THÊM MỚI HỌC VIÊN ===");

        Student student = new Student();

        // Nhập họ tên
        while (true) {
            System.out.print("Họ tên: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Họ tên không được để trống!");
                continue;
            }
            student.setName(name);
            break;
        }

        // Nhập ngày sinh
        while (true) {
            System.out.print("Ngày sinh (yyyy-MM-dd): ");
            String dobStr = scanner.nextLine().trim();
            try {
                LocalDate dob = LocalDate.parse(dobStr);
                student.setDob(dob);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ! Vui lòng nhập theo yyyy-MM-dd");
            }
        }

        // Nhập email
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email không được để trống!");
                continue;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Email không đúng định dạng!");
                continue;
            }
            if (studentService.isEmailExists(email)) {
                System.out.println("Email đã tồn tại trong hệ thống!");
                continue;
            }
            student.setEmail(email);
            break;
        }

        // Nhập giới tính
        while (true) {
            System.out.print("Giới tính (1-Nam, 0-Nữ): ");
            String sexStr = scanner.nextLine().trim();
            if (sexStr.equals("1") || sexStr.equals("0")) {
                student.setSex(Integer.parseInt(sexStr));
                break;
            } else {
                System.out.println("Vui lòng nhập 1 (Nam) hoặc 0 (Nữ)!");
            }
        }

        // Nhập số điện thoại
        while (true) {
            System.out.print("Số điện thoại: ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("Số điện thoại không được để trống!");
                continue;
            }
            if (!phone.matches("^(0|\\+84)\\d{9,10}$")) {
                System.out.println("Số điện thoại không hợp lệ!");
                continue;
            }
            student.setPhone(phone);
            break;
        }

        // Nhập mật khẩu
        while (true) {
            System.out.print("Mật khẩu (tối thiểu 6 ký tự): ");
            String password = scanner.nextLine().trim();
            if (password.length() < 6) {
                System.out.println("Mật khẩu phải có ít nhất 6 ký tự!");
                continue;
            }
            student.setPassword(password);
            break;
        }

        student.setId(studentService.generateNewStudentId());
        student.setCreateAt(LocalDate.now());

        if (studentService.addStudent(student)) {
            System.out.println("✓ Thêm học viên thành công! Mã học viên: " + student.getId());
        } else {
            System.out.println("✗ Thêm học viên thất bại!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void editStudent() {
        System.out.println("\n=== CHỈNH SỬA HỌC VIÊN ===");

        int id;
        while (true) {
            System.out.print("Nhập ID học viên cần sửa: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy học viên với ID: " + id);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nThông tin hiện tại:");
        System.out.println("Họ tên: " + student.getName());
        System.out.println("Ngày sinh: " + student.getDob());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Giới tính: " + student.getSexAsString());
        System.out.println("Số điện thoại: " + student.getPhone());

        System.out.println("\n=== MENU CHỈNH SỬA ===");
        System.out.println("1. Sửa họ tên");
        System.out.println("2. Sửa ngày sinh");
        System.out.println("3. Sửa email");
        System.out.println("4. Sửa giới tính");
        System.out.println("5. Sửa số điện thoại");
        System.out.println("6. Sửa tất cả");
        System.out.println("7. Hủy");
        System.out.print("Chọn thuộc tính cần sửa: ");

        String choice = scanner.nextLine().trim();

        boolean updated = false;

        switch (choice) {
            case "1":
                System.out.print("Họ tên mới: ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) {
                    student.setName(newName);
                    updated = true;
                }
                break;

            case "2":
                System.out.print("Ngày sinh mới (yyyy-MM-dd): ");
                try {
                    LocalDate newDob = LocalDate.parse(scanner.nextLine().trim());
                    student.setDob(newDob);
                    updated = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Định dạng ngày không hợp lệ!");
                }
                break;

            case "3":
                System.out.print("Email mới: ");
                String newEmail = scanner.nextLine().trim();
                if (!newEmail.isEmpty() && !newEmail.equals(student.getEmail())) {
                    if (!studentService.isEmailExists(newEmail)) {
                        student.setEmail(newEmail);
                        updated = true;
                    } else {
                        System.out.println("Email đã tồn tại!");
                    }
                }
                break;

            case "4":
                System.out.print("Giới tính mới (1-Nam, 0-Nữ): ");
                String newSex = scanner.nextLine().trim();
                if (newSex.equals("1") || newSex.equals("0")) {
                    student.setSex(Integer.parseInt(newSex));
                    updated = true;
                }
                break;

            case "5":
                System.out.print("Số điện thoại mới: ");
                String newPhone = scanner.nextLine().trim();
                if (!newPhone.isEmpty()) {
                    student.setPhone(newPhone);
                    updated = true;
                }
                break;

            case "6":
                // Sửa họ tên
                while (true) {
                    System.out.print("Họ tên mới: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Họ tên không được để trống!");
                        continue;
                    }
                    student.setName(name);
                    break;
                }

                // Sửa ngày sinh
                while (true) {
                    System.out.print("Ngày sinh mới (yyyy-MM-dd): ");
                    try {
                        LocalDate dob = LocalDate.parse(scanner.nextLine().trim());
                        student.setDob(dob);
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Định dạng ngày không hợp lệ!");
                    }
                }

                // Sửa email
                while (true) {
                    System.out.print("Email mới: ");
                    String email = scanner.nextLine().trim();
                    if (email.isEmpty()) {
                        System.out.println("Email không được để trống!");
                        continue;
                    }
                    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        System.out.println("Email không đúng định dạng!");
                        continue;
                    }
                    if (!email.equals(student.getEmail()) && studentService.isEmailExists(email)) {
                        System.out.println("Email đã tồn tại!");
                        continue;
                    }
                    student.setEmail(email);
                    break;
                }

                // Sửa giới tính
                while (true) {
                    System.out.print("Giới tính mới (1-Nam, 0-Nữ): ");
                    String sex = scanner.nextLine().trim();
                    if (sex.equals("1") || sex.equals("0")) {
                        student.setSex(Integer.parseInt(sex));
                        break;
                    } else {
                        System.out.println("Vui lòng nhập 1 (Nam) hoặc 0 (Nữ)!");
                    }
                }

                // Sửa số điện thoại
                while (true) {
                    System.out.print("Số điện thoại mới: ");
                    String phone = scanner.nextLine().trim();
                    if (phone.isEmpty()) {
                        System.out.println("Số điện thoại không được để trống!");
                        continue;
                    }
                    if (!phone.matches("^(0|\\+84)\\d{9,10}$")) {
                        System.out.println("Số điện thoại không hợp lệ!");
                        continue;
                    }
                    student.setPhone(phone);
                    break;
                }

                updated = true;
                break;

            case "7":
                System.out.println("Hủy chỉnh sửa.");
                return;

            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        if (updated && studentService.updateStudent(student)) {
            System.out.println("✓ Cập nhật học viên thành công!");
        } else if (updated) {
            System.out.println("✗ Cập nhật thất bại!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void deleteStudent() {
        System.out.println("\n=== XÓA HỌC VIÊN ===");

        int id;
        while (true) {
            System.out.print("Nhập ID học viên cần xóa: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy học viên với ID: " + id);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nThông tin học viên:");
        System.out.println("ID: " + student.getId());
        System.out.println("Họ tên: " + student.getName());
        System.out.println("Email: " + student.getEmail());

        // Kiểm tra đăng ký khóa học
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(id);
        if (!enrollments.isEmpty()) {
            System.out.println("\n⚠ Học viên này đã đăng ký " + enrollments.size() + " khóa học!");
            System.out.println("Không thể xóa học viên đã đăng ký khóa học.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.print("\nBạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            if (studentService.deleteStudent(id)) {
                System.out.println("✓ Xóa học viên thành công!");
            } else {
                System.out.println("✗ Xóa học viên thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void searchStudents() {
        System.out.println("\n=== TÌM KIẾM HỌC VIÊN ===");
        System.out.print("Nhập từ khóa tìm kiếm (tên, email hoặc ID): ");
        String keyword = scanner.nextLine().trim();

        List<Student> results = studentService.searchStudents(keyword);

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy học viên nào với từ khóa: " + keyword);
        } else {
            System.out.println("\nKết quả tìm kiếm (" + results.size() + " học viên):");
            TableFormatter.printStudentTable(results);
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void sortStudents() {
        System.out.println("\n=== SẮP XẾP HỌC VIÊN ===");
        System.out.println("Sắp xếp theo:");
        System.out.println("1. Tên");
        System.out.println("2. ID");
        System.out.print("Chọn: ");

        String sortChoice = scanner.nextLine().trim();
        String sortBy = sortChoice.equals("1") ? "name" : "id";

        System.out.println("\nThứ tự:");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Chọn: ");

        String orderChoice = scanner.nextLine().trim();
        boolean ascending = orderChoice.equals("1");

        List<Student> sorted = studentService.sortStudents(sortBy, ascending);

        System.out.println("\nDanh sách đã sắp xếp:");
        TableFormatter.printStudentTable(sorted);

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    // ==================== QUẢN LÝ ĐĂNG KÝ ====================
    private void showEnrollmentManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("            QUẢN LÝ ĐĂNG KÝ KHÓA HỌC");
            System.out.println("=".repeat(60));
            System.out.println("1. Hiển thị học viên theo từng khóa học");
            System.out.println("2. Duyệt học viên đăng ký khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học");
            System.out.println("4. Quay lại menu chính");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayEnrollmentsByCourse();
                    break;
                case "2":
                    approveEnrollment();
                    break;
                case "3":
                    removeStudentFromCourse();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayEnrollmentsByCourse() {
        System.out.println("\n=== DANH SÁCH HỌC VIÊN THEO KHÓA HỌC ===");

        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("Chưa có khóa học nào.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        for (Course course : courses) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("KHÓA HỌC: " + course.getName() + " (ID: " + course.getId() + ")");
            System.out.println("Giảng viên: " + course.getInstructor());

            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(course.getId());
            if (enrollments.isEmpty()) {
                System.out.println("Chưa có học viên đăng ký.");
            } else {
                System.out.println("\nDanh sách đăng ký:");
                for (Enrollment e : enrollments) {
                    Student student = studentService.getStudentById(e.getStudentId());
                    if (student != null) {
                        System.out.printf("  • %s (ID: %d) - %s - %s%n",
                                student.getName(),
                                student.getId(),
                                e.getRegisteredAt().toLocalDate(),
                                e.getStatusInVietnamese());
                    }
                }
            }
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void approveEnrollment() {
        System.out.println("\n=== DUYỆT ĐĂNG KÝ KHÓA HỌC ===");

        // Hiển thị các đăng ký đang chờ duyệt
        List<Enrollment> allEnrollments = enrollmentService.getAllEnrollmentsWithDetails();
        List<Enrollment> waitingEnrollments = allEnrollments.stream()
                .filter(e -> "WAITING".equals(e.getStatus()))
                .toList();

        if (waitingEnrollments.isEmpty()) {
            System.out.println("Không có đăng ký nào đang chờ duyệt.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nCác đăng ký đang chờ duyệt:");
        for (Enrollment e : waitingEnrollments) {
            System.out.printf("ID: %d - %s đăng ký khóa %s (%s)%n",
                    e.getId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getRegisteredAt().toLocalDate());
        }

        int enrollmentId;
        while (true) {
            System.out.print("\nNhập ID đăng ký cần duyệt: ");
            try {
                enrollmentId = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        int finalEnrollmentId = enrollmentId;
        Enrollment selected = waitingEnrollments.stream()
                .filter(e -> e.getId() == finalEnrollmentId)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("Không tìm thấy đăng ký với ID: " + enrollmentId);
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n1. Xác nhận");
        System.out.println("2. Từ chối");
        System.out.print("Chọn hành động: ");

        String action = scanner.nextLine().trim();
        String status = action.equals("1") ? "CONFIRMED" : "DENIED";

        if (enrollmentService.updateEnrollmentStatus(enrollmentId, status)) {
            System.out.println("✓ Cập nhật trạng thái thành công!");
        } else {
            System.out.println("✗ Cập nhật thất bại!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void removeStudentFromCourse() {
        System.out.println("\n=== XÓA HỌC VIÊN KHỎI KHÓA HỌC ===");

        int studentId;
        while (true) {
            System.out.print("Nhập ID học viên: ");
            try {
                studentId = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        // Hiển thị các khóa học học viên đã đăng ký
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollmentsWithCourseInfo(studentId);
        if (enrollments.isEmpty()) {
            System.out.println("Học viên chưa đăng ký khóa học nào.");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("\nCác khóa học " + student.getName() + " đã đăng ký:");
        for (Enrollment e : enrollments) {
            System.out.printf("ID ĐK: %d - %s (Trạng thái: %s)%n",
                    e.getId(),
                    e.getCourseName(),
                    e.getStatusInVietnamese());
        }

        int enrollmentId;
        while (true) {
            System.out.print("\nNhập ID đăng ký cần xóa: ");
            try {
                enrollmentId = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ!");
            }
        }

        int finalEnrollmentId = enrollmentId;
        Enrollment selected = enrollments.stream()
                .filter(e -> e.getId() == finalEnrollmentId)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("Không tìm thấy đăng ký!");
            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            if (enrollmentService.deleteEnrollment(enrollmentId)) {
                System.out.println("✓ Xóa học viên khỏi khóa học thành công!");
            } else {
                System.out.println("✗ Xóa thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    // ==================== THỐNG KÊ ====================
    private void showStatisticsMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    THỐNG KÊ");
            System.out.println("=".repeat(60));
            System.out.println("1. Thống kê tổng số lượng khóa học và học viên");
            System.out.println("2. Thống kê học viên theo từng khóa học");
            System.out.println("3. Top 5 khóa học đông học viên nhất");
            System.out.println("4. Liệt kê khóa học có trên 10 học viên");
            System.out.println("5. Quay lại menu chính");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTotalStatistics();
                    break;
                case "2":
                    showStudentsPerCourse();
                    break;
                case "3":
                    showTopCourses();
                    break;
                case "4":
                    showCoursesWithMoreThan10Students();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void showTotalStatistics() {
        System.out.println("\n=== THỐNG KÊ TỔNG QUAN ===");

        int totalCourses = courseService.getTotalCourses();
        int totalStudents = studentService.getTotalStudents();
        int totalConfirmed = enrollmentService.getTotalConfirmedStudents();

        System.out.println("\n" + "-".repeat(40));
        System.out.printf("Tổng số khóa học: %d%n", totalCourses);
        System.out.printf("Tổng số học viên: %d%n", totalStudents);
        System.out.printf("Tổng số lượt đăng ký đã xác nhận: %d%n", totalConfirmed);
        System.out.println("-".repeat(40));

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void showStudentsPerCourse() {
        System.out.println("\n=== THỐNG KÊ HỌC VIÊN THEO KHÓA HỌC ===");

        List<Object[]> stats = enrollmentService.getStudentCountPerCourseForStats();

        String[] headers = {"Mã KH", "Tên khóa học", "Số học viên"};
        TableFormatter.printStatisticsTable("THỐNG KÊ HỌC VIÊN THEO KHÓA HỌC", stats, headers);

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void showTopCourses() {
        System.out.println("\n=== TOP 5 KHÓA HỌC ĐÔNG HỌC VIÊN NHẤT ===");

        List<Course> topCourses = courseService.getTopCourses(5);

        if (topCourses.isEmpty()) {
            System.out.println("Không có dữ liệu.");
        } else {
            System.out.println("\n" + "-".repeat(60));
            System.out.printf("%-5s %-30s %-15s%n", "STT", "Tên khóa học", "Số học viên");
            System.out.println("-".repeat(60));

            int stt = 1;
            for (Course course : topCourses) {
                int count = enrollmentService.getStudentCountByCourseId(course.getId());
                System.out.printf("%-5d %-30s %-15d%n", stt++, course.getName(), count);
            }
            System.out.println("-".repeat(60));
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void showCoursesWithMoreThan10Students() {
        System.out.println("\n=== KHÓA HỌC CÓ TRÊN 10 HỌC VIÊN ===");

        List<Course> courses = courseService.getCoursesWithMoreThan(10);

        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào có trên 10 học viên.");
        } else {
            System.out.println("\n" + "-".repeat(60));
            System.out.printf("%-5s %-30s %-15s%n", "Mã KH", "Tên khóa học", "Số học viên");
            System.out.println("-".repeat(60));

            for (Course course : courses) {
                int count = enrollmentService.getStudentCountByCourseId(course.getId());
                System.out.printf("%-5d %-30s %-15d%n", course.getId(), course.getName(), count);
            }
            System.out.println("-".repeat(60));
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }
}
