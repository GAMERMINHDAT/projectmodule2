package ra.Coursemanagement.dao;

import ra.Coursemanagement.model.Admin;

public interface IAdminDAO {
    Admin getAdminByUsername(String username);
    boolean checkLogin(String username, String password);
    boolean updatePassword(int adminId, String newPassword);
}