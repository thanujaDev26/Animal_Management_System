package model;

import entity.Admin;

public interface AdminModel {
    boolean saveAdmin(Admin admin);

    Admin searchAdmin(String term);

    boolean updateAdmin(Admin animal);
}
