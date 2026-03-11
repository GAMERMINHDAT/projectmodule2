import ra.Coursemanagement.presentation.LoginView;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         HỆ THỐNG QUẢN LÝ ĐÀO TẠO - COURSE MANAGEMENT      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        LoginView loginView = new LoginView();
        loginView.showLoginMenu();
    }
}