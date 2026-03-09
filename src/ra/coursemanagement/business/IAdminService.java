package ra.coursemanagement.business;

import ra.coursemanagement.model.Admin;

public interface IAdminService {
    Admin login(String username, String password);
}