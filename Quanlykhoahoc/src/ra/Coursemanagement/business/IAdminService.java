package ra.Coursemanagement.business;

import ra.Coursemanagement.model.Admin;

public interface IAdminService {
    boolean checkLogin(String username, String password);
    Admin getAdminByUsername(String username);
    boolean updatePassword(String username, String oldPassword, String newPassword);
}