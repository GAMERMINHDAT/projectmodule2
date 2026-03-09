package ra.coursemanagement.presentation;

import ra.coursemanagement.model.Admin;
import ra.coursemanagement.model.Student;
import ra.coursemanagement.business.IAdminService;
import ra.coursemanagement.business.IStudentService;
import ra.coursemanagement.business.impl.AdminServiceImpl;
import ra.coursemanagement.business.impl.StudentServiceImpl;
import ra.coursemanagement.utils.InputValidator;

public class MainView {
    private IAdminService adminService = new AdminServiceImpl();
    private IStudentService studentService = new StudentServiceImpl();
    private AdminView adminView = new AdminView();
    private StudentView studentView = new StudentView();

    public void showMainMenu() {
        while (true) {
            System.out.println("\n======== HỆ THỐNG QUẢN LÝ ĐÀO TẠO ========");
            System.out.println("1. Đăng nhập với tư cách Quản trị viên");
            System.out.println("2. Đăng nhập với tư cách Học viên");
            System.out.println("3. Thoát");
            System.out.println("=============================================");

            int choice = InputValidator.getIntInput("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    studentLogin();
                    break;
                case 3:
                    System.out.println("Tạm biệt!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void adminLogin() {
        System.out.println("\n=== ĐĂNG NHẬP QUẢN TRỊ VIÊN ===");
        String username = InputValidator.getStringInput("Tên đăng nhập: ");
        String password = InputValidator.getStringInput("Mật khẩu: ");

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Tên đăng nhập và mật khẩu không được để trống!");
            return;
        }

        Admin admin = adminService.login(username, password);
        if (admin != null) {
            System.out.println("Đăng nhập thành công!");
            adminView.showAdminMenu();
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private void studentLogin() {
        System.out.println("\n=== ĐĂNG NHẬP HỌC VIÊN ===");
        String email = InputValidator.getStringInput("Email: ");
        String password = InputValidator.getStringInput("Mật khẩu: ");

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email và mật khẩu không được để trống!");
            return;
        }

        Student student = studentService.login(email, password);
        if (student != null) {
            System.out.println("Đăng nhập thành công!");
            studentView.setCurrentStudent(student);
            studentView.showStudentMenu();
        } else {
            System.out.println("Sai email hoặc mật khẩu!");
        }
    }
}