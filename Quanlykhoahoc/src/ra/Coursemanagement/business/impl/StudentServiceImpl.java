package ra.Coursemanagement.business.impl;

import ra.Coursemanagement.business.IStudentService;
import ra.Coursemanagement.dao.IStudentDAO;
import ra.Coursemanagement.dao.impl.StudentDAOImpl;
import ra.Coursemanagement.model.Student;

import java.time.LocalDate;
import java.util.List;

public class StudentServiceImpl implements IStudentService {

    private IStudentDAO studentDAO;

    public StudentServiceImpl() {
        this.studentDAO = new StudentDAOImpl();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    @Override
    public Student getStudentByEmail(String email) {
        return studentDAO.getStudentByEmail(email);
    }

    @Override
    public boolean addStudent(Student student) {
        if (!validateStudentData(student)) {
            return false;
        }

        if (isEmailExists(student.getEmail())) {
            return false;
        }

        if (student.getId() <= 0) {
            student.setId(generateNewStudentId());
        }

        if (student.getCreateAt() == null) {
            student.setCreateAt(LocalDate.now());
        }

        return studentDAO.addStudent(student);
    }

    @Override
    public boolean updateStudent(Student student) {
        if (!validateStudentData(student)) {
            return false;
        }

        Student existingStudent = studentDAO.getStudentById(student.getId());
        if (existingStudent == null) {
            return false;
        }

        // Kiểm tra email đã tồn tại chưa (nếu thay đổi email)
        if (!existingStudent.getEmail().equals(student.getEmail())) {
            if (isEmailExists(student.getEmail())) {
                return false;
            }
        }

        return studentDAO.updateStudent(student);
    }

    @Override
    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentDAO.searchStudents(keyword.trim());
    }

    @Override
    public List<Student> sortStudents(String sortBy, boolean ascending) {
        return studentDAO.sortStudents(sortBy, ascending);
    }

    @Override
    public int getTotalStudents() {
        return studentDAO.getTotalStudents();
    }

    @Override
    public boolean updatePassword(int studentId, String oldPassword, String newPassword) {
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            return false;
        }

        // Kiểm tra mật khẩu cũ
        if (!student.getPassword().equals(oldPassword)) {
            return false;
        }

        // Validate mật khẩu mới
        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            return false;
        }

        return studentDAO.updatePassword(studentId, newPassword);
    }

    @Override
    public boolean checkLogin(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        return studentDAO.checkLogin(email, password);
    }

    @Override
    public boolean isEmailExists(String email) {
        return studentDAO.getStudentByEmail(email) != null;
    }

    @Override
    public int generateNewStudentId() {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (Student s : students) {
            if (s.getId() > maxId) {
                maxId = s.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public boolean validateStudentData(Student student) {
        if (student == null) {
            return false;
        }

        // Kiểm tra tên
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return false;
        }

        // Kiểm tra email
        if (student.getEmail() == null || student.getEmail().trim().isEmpty() ||
                !student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }

        // Kiểm tra ngày sinh
        if (student.getDob() == null) {
            return false;
        }

        // Kiểm tra giới tính
        if (student.getSex() != 0 && student.getSex() != 1) {
            return false;
        }

        // Kiểm tra số điện thoại
        if (student.getPhone() == null || student.getPhone().trim().isEmpty()) {
            return false;
        }

        // Kiểm tra mật khẩu
        if (student.getPassword() == null || student.getPassword().trim().isEmpty() ||
                student.getPassword().length() < 6) {
            return false;
        }

        return true;
    }
}