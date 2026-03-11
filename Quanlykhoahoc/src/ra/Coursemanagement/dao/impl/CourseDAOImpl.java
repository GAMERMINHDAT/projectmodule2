package ra.Coursemanagement.dao.impl;

import ra.Coursemanagement.dao.ICourseDAO;
import ra.Coursemanagement.model.Course;
import ra.Coursemanagement.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements ICourseDAO {

    @Override
    public List<Course> getAllCourses() {
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
    public Course getCourseById(int id) {
        String sql = "SELECT * FROM Course WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Course getCourseByName(String name) {
        String sql = "SELECT * FROM Course WHERE name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO Course (id, name, duration, instructor, create_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, course.getId());
            pstmt.setString(2, course.getName());
            pstmt.setInt(3, course.getDuration());
            pstmt.setString(4, course.getInstructor());
            pstmt.setDate(5, Date.valueOf(course.getCreateAt() != null ? course.getCreateAt() : LocalDate.now()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCourse(Course course) {
        String sql = "UPDATE Course SET name = ?, duration = ?, instructor = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getDuration());
            pstmt.setString(3, course.getInstructor());
            pstmt.setInt(4, course.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM Course WHERE id = ?";

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
    public List<Course> searchCourses(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE LOWER(name) LIKE ? ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> sortCourses(String sortBy, boolean ascending) {
        List<Course> courses = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC";
        String column = sortBy.equalsIgnoreCase("name") ? "name" : "id";
        String sql = "SELECT * FROM Course ORDER BY " + column + " " + order;

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

    @Override
    public List<Course> getTopCourses(int limit) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(e.student_id) as student_count " +
                "FROM Course c " +
                "LEFT JOIN Enrollment e ON c.id = e.course_id " +
                "GROUP BY c.id " +
                "ORDER BY student_count DESC " +
                "LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> getCoursesWithMoreThan(int studentCount) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(e.student_id) as student_count " +
                "FROM Course c " +
                "LEFT JOIN Enrollment e ON c.id = e.course_id " +
                "GROUP BY c.id " +
                "HAVING COUNT(e.student_id) > ? " +
                "ORDER BY student_count DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentCount);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        course.setDuration(rs.getInt("duration"));
        course.setInstructor(rs.getString("instructor"));
        course.setCreateAt(rs.getDate("create_at") != null ? rs.getDate("create_at").toLocalDate() : null);
        return course;
    }
}