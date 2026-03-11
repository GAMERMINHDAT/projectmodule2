package ra.Coursemanagement.utils;

import ra.Coursemanagement.model.Course;
import ra.Coursemanagement.model.Enrollment;
import ra.Coursemanagement.model.Student;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TableFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void printStudentTable(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("┌─────────────────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                            Không có dữ liệu học viên                                       │");
            System.out.println("└─────────────────────────────────────────────────────────────────────────────────────────────┘");
            return;
        }

        // Định dạng bảng
        String format = "│ %-5s │ %-25s │ %-12s │ %-25s │ %-6s │ %-15s │%n";
        String line = "├───────┼───────────────────────────┼──────────────┼───────────────────────────┼────────┼─────────────────┤";
        String topLine = "┌───────┬───────────────────────────┬──────────────┬───────────────────────────┬────────┬─────────────────┐";
        String bottomLine = "└───────┴───────────────────────────┴──────────────┴───────────────────────────┴────────┴─────────────────┘";

        System.out.println(topLine);
        System.out.printf("│ %-5s │ %-25s │ %-12s │ %-25s │ %-6s │ %-15s │%n",
                "ID", "Họ tên", "Ngày sinh", "Email", "Giới tính", "Số điện thoại");
        System.out.println(line);

        for (Student s : students) {
            String dob = s.getDob() != null ? s.getDob().format(DATE_FORMATTER) : "N/A";
            System.out.printf(format,
                    s.getId(),
                    truncate(s.getName(), 25),
                    dob,
                    truncate(s.getEmail(), 25),
                    s.getSexAsString(),
                    s.getPhone() != null ? s.getPhone() : "N/A");
        }
        System.out.println(bottomLine);
    }

    public static void printCourseTable(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            System.out.println("┌────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                            Không có dữ liệu khóa học                           │");
            System.out.println("└────────────────────────────────────────────────────────────────────────────────┘");
            return;
        }

        String format = "│ %-5s │ %-30s │ %-12s │ %-25s │ %-12s │%n";
        String line = "├───────┼────────────────────────────────┼──────────────┼───────────────────────────┼──────────────┤";
        String topLine = "┌───────┬────────────────────────────────┬──────────────┬───────────────────────────┬──────────────┐";
        String bottomLine = "└───────┴────────────────────────────────┴──────────────┴───────────────────────────┴──────────────┘";

        System.out.println(topLine);
        System.out.printf("│ %-5s │ %-30s │ %-12s │ %-25s │ %-12s │%n",
                "ID", "Tên khóa học", "Thời lượng", "Giảng viên", "Ngày tạo");
        System.out.println(line);

        for (Course c : courses) {
            String createAt = c.getCreateAt() != null ? c.getCreateAt().format(DATE_FORMATTER) : "N/A";
            System.out.printf(format,
                    c.getId(),
                    truncate(c.getName(), 30),
                    c.getDuration() + " giờ",
                    truncate(c.getInstructor(), 25),
                    createAt);
        }
        System.out.println(bottomLine);
    }

    public static void printEnrollmentTable(List<Enrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                            Không có dữ liệu đăng ký                                                         │");
            System.out.println("└─────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
            return;
        }

        String format = "│ %-5s │ %-25s │ %-30s │ %-20s │ %-15s │%n";
        String line = "├───────┼───────────────────────────┼────────────────────────────────┼──────────────────────┼─────────────────┤";
        String topLine = "┌───────┬───────────────────────────┬────────────────────────────────┬──────────────────────┬─────────────────┐";
        String bottomLine = "└───────┴───────────────────────────┴────────────────────────────────┴──────────────────────┴─────────────────┘";

        System.out.println(topLine);
        System.out.printf("│ %-5s │ %-25s │ %-30s │ %-20s │ %-15s │%n",
                "ID", "Học viên", "Khóa học", "Ngày đăng ký", "Trạng thái");
        System.out.println(line);

        for (Enrollment e : enrollments) {
            String registeredAt = e.getRegisteredAt() != null ?
                    e.getRegisteredAt().format(DATETIME_FORMATTER) : "N/A";
            String studentName = e.getStudentName() != null ? e.getStudentName() : "ID: " + e.getStudentId();
            String courseName = e.getCourseName() != null ? e.getCourseName() : "ID: " + e.getCourseId();

            System.out.printf(format,
                    e.getId(),
                    truncate(studentName, 25),
                    truncate(courseName, 30),
                    registeredAt,
                    e.getStatusInVietnamese());
        }
        System.out.println(bottomLine);
    }

    public static void printStatisticsTable(String title, List<Object[]> data, String[] headers) {
        if (data == null || data.isEmpty()) {
            System.out.println("┌─────────────────────────────────────────────┐");
            System.out.println("│            Không có dữ liệu                 │");
            System.out.println("└─────────────────────────────────────────────┘");
            return;
        }

        // Tính độ rộng cột
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (Object[] row : data) {
            for (int i = 0; i < row.length && i < headers.length; i++) {
                int len = row[i].toString().length();
                if (len > colWidths[i]) {
                    colWidths[i] = len;
                }
            }
        }

        // Vẽ bảng
        System.out.println("\n" + title);
        StringBuilder lineBuilder = new StringBuilder("├");
        StringBuilder topBuilder = new StringBuilder("┌");
        StringBuilder bottomBuilder = new StringBuilder("└");

        for (int i = 0; i < headers.length; i++) {
            lineBuilder.append("─".repeat(colWidths[i] + 2)).append("┼");
            topBuilder.append("─".repeat(colWidths[i] + 2)).append("┬");
            bottomBuilder.append("─".repeat(colWidths[i] + 2)).append("┴");
        }

        String line = lineBuilder.toString().substring(0, lineBuilder.length() - 1) + "┤";
        String topLine = topBuilder.toString().substring(0, topBuilder.length() - 1) + "┐";
        String bottomLine = bottomBuilder.toString().substring(0, bottomBuilder.length() - 1) + "┘";

        System.out.println(topLine);

        // In header
        System.out.print("│");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-" + colWidths[i] + "s │", headers[i]);
        }
        System.out.println();
        System.out.println(line);

        // In dữ liệu
        for (Object[] row : data) {
            System.out.print("│");
            for (int i = 0; i < row.length && i < headers.length; i++) {
                System.out.printf(" %-" + colWidths[i] + "s │", row[i].toString());
            }
            System.out.println();
        }
        System.out.println(bottomLine);
    }

    private static String truncate(String str, int length) {
        if (str == null) return "N/A";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}