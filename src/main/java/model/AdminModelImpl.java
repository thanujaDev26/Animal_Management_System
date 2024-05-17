package model;

import db.DBConnection;
import dev.morphia.Datastore;
import entity.Admin;
import entity.Animal;
import dev.morphia.query.filters.Filters;

public class AdminModelImpl implements AdminModel{
    private static final Datastore datastore = DBConnection.getInstance().getDatastore();

    @Override
    public boolean saveAdmin(Admin admin) {
        try {
            datastore.save(admin);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Admin searchAdmin(String uname) {
        return datastore.find(Admin.class).filter(Filters.eq("uname", uname)).first();
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return false;
    }
}
