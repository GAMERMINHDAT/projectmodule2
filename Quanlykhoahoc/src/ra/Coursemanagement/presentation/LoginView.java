package ra.Coursemanagement.presentation;

import ra.Coursemanagement.business.IAdminService;
import ra.Coursemanagement.business.IStudentService;
import ra.Coursemanagement.business.impl.AdminServiceImpl;
import ra.Coursemanagement.business.impl.StudentServiceImpl;

import java.util.Scanner;

public class LoginView {
    private Scanner scanner;
    private IAdminService adminService;
    private IStudentService studentService;
    private AdminView adminView;
    private StudentView studentView;

    public LoginView() {
        this.scanner = new Scanner(System.in);
        this.adminService = new AdminServiceImpl();
        this.studentService = new StudentServiceImpl();
        this.adminView = new AdminView();
        this.studentView = new StudentView();
    }

    public void showLoginMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("            HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
            System.out.println("=".repeat(60));
            System.out.println("1. Đăng nhập với tư cách Quản trị viên");
            System.out.println("2. Đăng nhập với tư cách Học viên");
            System.out.println("3. Thoát");
            System.out.println("=".repeat(60));
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    adminLogin();
                    break;
                case "2":
                    studentLogin();
                    break;
                case "3":
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập lại.");
            }
        }
    }

    private void adminLogin() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("ĐĂNG NHẬP QUẢN TRỊ VIÊN");
        System.out.println("-".repeat(40));

        String username;
        while (true) {
            System.out.print("Tên đăng nhập: ");
            username = scanner.nextLine().trim();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Tên đăng nhập không được để trống!");
        }

        String password;
        while (true) {
            System.out.print("Mật khẩu: ");
            password = scanner.nextLine().trim();
            if (!password.isEmpty()) {
                break;
            }
            System.out.println("Mật khẩu không được để trống!");
        }

        if (adminService.checkLogin(username, password)) {
            System.out.println("\n✓ Đăng nhập thành công!");
            adminView.showMenu();
        } else {
            System.out.println("\n✗ Đăng nhập thất bại! Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    private void studentLogin() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("ĐĂNG NHẬP HỌC VIÊN");
        System.out.println("-".repeat(40));

        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                break;
            }
            System.out.println("Email không được để trống!");
        }

        String password;
        while (true) {
            System.out.print("Mật khẩu: ");
            password = scanner.nextLine().trim();
            if (!password.isEmpty()) {
                break;
            }
            System.out.println("Mật khẩu không được để trống!");
        }

        if (studentService.checkLogin(email, password)) {
            System.out.println("\n✓ Đăng nhập thành công!");
            studentView.setLoggedInStudent(email);
            studentView.showMenu();
        } else {
            System.out.println("\n✗ Đăng nhập thất bại! Sai email hoặc mật khẩu.");
        }
    }
}