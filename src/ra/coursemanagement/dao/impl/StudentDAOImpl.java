package ra.coursemanagement.dao.impl;

import ra.coursemanagement.dao.IStudentDAO;
import ra.coursemanagement.model.Student;
import ra.coursemanagement.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements IStudentDAO {

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public Student findById(int id) {
        String sql = "SELECT * FROM Student WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM Student WHERE email = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student findByUsernameAndPassword(String email, String password) {
        String sql = "SELECT * FROM Student WHERE email = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(Student student) {
        String sql = "INSERT INTO Student (id, name, dob, email, sex, phone, password, create_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setDate(3, Date.valueOf(student.getDob()));
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getSex());
            ps.setString(6, student.getPhone());
            ps.setString(7, student.getPassword());
            ps.setDate(8, Date.valueOf(student.getCreateAt() != null ? student.getCreateAt() : LocalDate.now()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Student student) {
        String sql = "UPDATE Student SET name = ?, dob = ?, email = ?, sex = ?, phone = ?, password = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setDate(2, Date.valueOf(student.getDob()));
            ps.setString(3, student.getEmail());
            ps.setInt(4, student.getSex());
            ps.setString(5, student.getPhone());
            ps.setString(6, student.getPassword());
            ps.setInt(7, student.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Student WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Student> search(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR CAST(id AS TEXT) LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public List<Student> sort(String field, String order) {
        List<Student> students = new ArrayList<>();
        String validField = field.equalsIgnoreCase("name") ? "name" : "id";
        String validOrder = order.equalsIgnoreCase("desc") ? "DESC" : "ASC";
        String sql = "SELECT * FROM Student ORDER BY " + validField + " " + validOrder;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setDob(rs.getDate("dob").toLocalDate());
        student.setEmail(rs.getString("email"));
        student.setSex(rs.getInt("sex"));
        student.setPhone(rs.getString("phone"));
        student.setPassword(rs.getString("password"));
        student.setCreateAt(rs.getDate("create_at").toLocalDate());
        return student;
    }
}