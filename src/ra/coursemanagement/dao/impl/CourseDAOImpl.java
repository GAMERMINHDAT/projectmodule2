package ra.coursemanagement.dao.impl;

import ra.coursemanagement.dao.ICourseDAO;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements ICourseDAO {

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course findById(int id) {
        String sql = "SELECT * FROM Course WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(Course course) {
        String sql = "INSERT INTO Course (id, name, duration, instructor, create_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, course.getId());
            ps.setString(2, course.getName());
            ps.setInt(3, course.getDuration());
            ps.setString(4, course.getInstructor());
            ps.setDate(5, Date.valueOf(course.getCreateAt() != null ? course.getCreateAt() : LocalDate.now()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Course course) {
        String sql = "UPDATE Course SET name = ?, duration = ?, instructor = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getName());
            ps.setInt(2, course.getDuration());
            ps.setString(3, course.getInstructor());
            ps.setInt(4, course.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Course WHERE id = ?";

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
    public List<Course> searchByName(String name) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE LOWER(name) LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> sort(String field, String order) {
        List<Course> courses = new ArrayList<>();
        String validField = field.equalsIgnoreCase("name") ? "name" : "id";
        String validOrder = order.equalsIgnoreCase("desc") ? "DESC" : "ASC";
        String sql = "SELECT * FROM Course ORDER BY " + validField + " " + validOrder;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public int getTotalCourses() {
        String sql = "SELECT COUNT(*) FROM Course";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        course.setDuration(rs.getInt("duration"));
        course.setInstructor(rs.getString("instructor"));
        course.setCreateAt(rs.getDate("create_at").toLocalDate());
        return course;
    }
}