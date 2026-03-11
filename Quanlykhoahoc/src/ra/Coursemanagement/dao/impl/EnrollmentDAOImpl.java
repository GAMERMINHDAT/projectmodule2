package ra.Coursemanagement.dao.impl;

import ra.Coursemanagement.dao.IEnrollmentDAO;
import ra.Coursemanagement.model.Enrollment;
import ra.Coursemanagement.utils.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements IEnrollmentDAO {

    @Override
    public List<Enrollment> getAllEnrollments() {
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
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment WHERE student_id = ? ORDER BY registered_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment WHERE course_id = ? ORDER BY registered_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsWithDetails() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, c.name as course_name " +
                "FROM Enrollment e " +
                "JOIN Student s ON e.student_id = s.id " +
                "JOIN Course c ON e.course_id = c.id " +
                "ORDER BY e.registered_at DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Enrollment enrollment = mapResultSetToEnrollment(rs);
                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseName(rs.getString("course_name"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        String sql = "SELECT * FROM Enrollment WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO Enrollment (id, student_id, course_id, registered_at, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getId());
            pstmt.setInt(2, enrollment.getStudentId());
            pstmt.setInt(3, enrollment.getCourseId());
            pstmt.setTimestamp(4, Timestamp.valueOf(enrollment.getRegisteredAt() != null ? enrollment.getRegisteredAt() : LocalDateTime.now()));
            pstmt.setString(5, enrollment.getStatus() != null ? enrollment.getStatus() : "WAITING");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        String sql = "UPDATE Enrollment SET status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, enrollmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEnrollment(int id) {
        String sql = "DELETE FROM Enrollment WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEnrollment(int studentId, int courseId) {
        String sql = "DELETE FROM Enrollment WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStudentEnrolled(int studentId, int courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollment WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Enrollment> getStudentEnrollmentsWithCourseInfo(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, c.name as course_name, c.instructor, c.duration " +
                "FROM Enrollment e " +
                "JOIN Course c ON e.course_id = c.id " +
                "WHERE e.student_id = ? " +
                "ORDER BY e.registered_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = mapResultSetToEnrollment(rs);
                    enrollment.setCourseName(rs.getString("course_name"));
                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public int getStudentCountByCourseId(int courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollment WHERE course_id = ? AND status = 'CONFIRMED'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT c.id, c.name, COUNT(e.student_id) as student_count " +
                "FROM Course c " +
                "LEFT JOIN Enrollment e ON c.id = e.course_id AND e.status = 'CONFIRMED' " +
                "GROUP BY c.id, c.name " +
                "ORDER BY student_count DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("name");
                row[2] = rs.getInt("student_count");
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getInt("id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setRegisteredAt(rs.getTimestamp("registered_at") != null ? rs.getTimestamp("registered_at").toLocalDateTime() : null);
        enrollment.setStatus(rs.getString("status"));
        return enrollment;
    }
}