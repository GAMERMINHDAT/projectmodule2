package ra.coursemanagement.dao;

import ra.coursemanagement.model.Admin;

public interface IAdminDAO {
    Admin findByUsername(String username);
}