package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.IAdminService;
import ra.coursemanagement.dao.IAdminDAO;
import ra.coursemanagement.dao.impl.AdminDAOImpl;
import ra.coursemanagement.model.Admin;

public class AdminServiceImpl implements IAdminService {
    private IAdminDAO adminDAO = new AdminDAOImpl();

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}