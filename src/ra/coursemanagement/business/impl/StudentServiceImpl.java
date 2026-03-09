package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.IStudentService;
import ra.coursemanagement.dao.IStudentDAO;
import ra.coursemanagement.dao.impl.StudentDAOImpl;
import ra.coursemanagement.model.Student;
import ra.coursemanagement.utils.InputValidator;

import java.util.List;

public class StudentServiceImpl implements IStudentService {
    private IStudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public Student getStudentByEmail(String email) {
        return studentDAO.findByEmail(email);
    }

    @Override
    public Student login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return studentDAO.findByUsernameAndPassword(email, password);
    }

    @Override
    public boolean addStudent(Student student) {
        // Validate dữ liệu
        if (!InputValidator.isValidName(student.getName())) {
            System.out.println("Tên không được để trống!");
            return false;
        }
        if (!InputValidator.isValidEmail(student.getEmail())) {
            System.out.println("Email không hợp lệ!");
            return false;
        }
        if (getStudentByEmail(student.getEmail()) != null) {
            System.out.println("Email đã tồn tại!");
            return false;
        }
        if (!InputValidator.isValidPhone(student.getPhone())) {
            System.out.println("Số điện thoại không hợp lệ!");
            return false;
        }
        if (!InputValidator.isValidPassword(student.getPassword())) {
            System.out.println("Mật khẩu không được để trống!");
            return false;
        }
        if (student.getDob() == null) {
            System.out.println("Ngày sinh không được để trống!");
            return false;
        }

        return studentDAO.add(student);
    }

    @Override
    public boolean updateStudent(Student student) {
        Student existingStudent = getStudentById(student.getId());
        if (existingStudent == null) {
            System.out.println("Không tìm thấy học viên!");
            return false;
        }

        // Validate dữ liệu
        if (!InputValidator.isValidName(student.getName())) {
            System.out.println("Tên không được để trống!");
            return false;
        }
        if (!InputValidator.isValidEmail(student.getEmail())) {
            System.out.println("Email không hợp lệ!");
            return false;
        }
        Student studentByEmail = getStudentByEmail(student.getEmail());
        if (studentByEmail != null && studentByEmail.getId() != student.getId()) {
            System.out.println("Email đã tồn tại!");
            return false;
        }
        if (!InputValidator.isValidPhone(student.getPhone())) {
            System.out.println("Số điện thoại không hợp lệ!");
            return false;
        }
        if (!InputValidator.isValidPassword(student.getPassword())) {
            System.out.println("Mật khẩu không được để trống!");
            return false;
        }

        return studentDAO.update(student);
    }

    @Override
    public boolean deleteStudent(int id) {
        Student student = getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return false;
        }
        return studentDAO.delete(id);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentDAO.search(keyword);
    }

    @Override
    public List<Student> sortStudents(String field, String order) {
        if (!field.equalsIgnoreCase("name") && !field.equalsIgnoreCase("id")) {
            field = "id";
        }
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            order = "asc";
        }
        return studentDAO.sort(field, order);
    }

    @Override
    public boolean changePassword(int studentId, String oldPassword, String newPassword) {
        Student student = getStudentById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy học viên!");
            return false;
        }

        if (!student.getPassword().equals(oldPassword)) {
            System.out.println("Mật khẩu cũ không đúng!");
            return false;
        }

        if (!InputValidator.isValidPassword(newPassword)) {
            System.out.println("Mật khẩu mới không được để trống!");
            return false;
        }

        student.setPassword(newPassword);
        return studentDAO.update(student);
    }

    @Override
    public int getNextId() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            return 1;
        }
        return students.stream().mapToInt(Student::getId).max().getAsInt() + 1;
    }
}