package ra.Coursemanagement.business.impl;

import ra.Coursemanagement.business.IAdminService;
import ra.Coursemanagement.dao.IAdminDAO;
import ra.Coursemanagement.dao.impl.AdminDAOImpl;
import ra.Coursemanagement.model.Admin;

public class AdminServiceImpl implements IAdminService {

    private IAdminDAO adminDAO;

    public AdminServiceImpl() {
        this.adminDAO = new AdminDAOImpl();
    }

    @Override
    public boolean checkLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }
        return adminDAO.checkLogin(username, password);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminDAO.getAdminByUsername(username);
    }

    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        Admin admin = adminDAO.getAdminByUsername(username);
        if (admin == null) {
            return false;
        }

        // Kiểm tra mật khẩu cũ
        if (!admin.getPassword().equals(oldPassword)) {
            return false;
        }

        // Validate mật khẩu mới
        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            return false;
        }

        return adminDAO.updatePassword(admin.getId(), newPassword);
    }
}
