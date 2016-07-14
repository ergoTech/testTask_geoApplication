package com.yura.c_simpl_lite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy S on 11.07.2016.
 */


public class CropFieldDao extends BaseDaoImpl<CropField, Integer>{

    public CropFieldDao(ConnectionSource connectionSource,
                        Class<CropField> dataClass) throws SQLException{
        super(connectionSource, dataClass);
    }

    public List<CropField> getAllCropFields() throws SQLException {
        return this.queryForAll();
    }
}