package ra.coursemanagement.dao.impl;

import ra.coursemanagement.dao.IEnrollmentDAO;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Enrollment;
import ra.coursemanagement.utils.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentDAOImpl implements IEnrollmentDAO {

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public Map<Course, Long> getStudentCountByCourse() {
        Map<Course, Long> result = new HashMap<>();
        String sql = "SELECT c.*, COUNT(e.student_id) as student_count " +
                "FROM Course c LEFT JOIN Enrollment e ON c.id = e.course_id " +
                "GROUP BY c.id ORDER BY student_count DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());

                long count = rs.getLong("student_count");
                result.put(course, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean add(Enrollment enrollment) {
        String sql = "INSERT INTO Enrollment (id, student_id, course_id, registered_at, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollment.getId());
            ps.setInt(2, enrollment.getStudentId());
            ps.setInt(3, enrollment.getCourseId());
            ps.setTimestamp(4, Timestamp.valueOf(enrollment.getRegisteredAt() != null ?
                    enrollment.getRegisteredAt() : LocalDateTime.now()));
            ps.setString(5, enrollment.getStatus() != null ? enrollment.getStatus() : "WAITING");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE Enrollment SET status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Enrollment WHERE id = ?";

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
    public boolean exists(int studentId, int courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollment WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Course> getTopCourses(int limit) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(e.student_id) as student_count " +
                "FROM Course c JOIN Enrollment e ON c.id = e.course_id " +
                "GROUP BY c.id ORDER BY student_count DESC LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(e.student_id) as student_count " +
                "FROM Course c JOIN Enrollment e ON c.id = e.course_id " +
                "GROUP BY c.id HAVING COUNT(e.student_id) > 10";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getInt("id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
        enrollment.setStatus(rs.getString("status"));
        return enrollment;
    }
}