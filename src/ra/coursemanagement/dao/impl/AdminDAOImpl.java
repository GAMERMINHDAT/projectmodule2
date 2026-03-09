package ra.coursemanagement.dao.impl;

import ra.coursemanagement.dao.IAdminDAO;
import ra.coursemanagement.model.Admin;
import ra.coursemanagement.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAOImpl implements IAdminDAO {

    @Override
    public Admin findByUsername(String username) {
        String sql = "SELECT * FROM Admin WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}