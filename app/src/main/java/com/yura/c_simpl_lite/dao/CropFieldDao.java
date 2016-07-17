package com.yura.c_simpl_lite.dao;

import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy S on 11.07.2016.
 */


public class CropFieldDao extends BaseDaoImpl<CropField, Integer>{
   private static final String TAG = "orm";
    public CropFieldDao(ConnectionSource connectionSource,
                        Class<CropField> dataClass) throws SQLException{
        super(connectionSource, dataClass);
    }

    public List<CropField> getAllCropFields() throws SQLException {
        return this.queryForAll();
    }
    public CropField getNext(int id) throws SQLException {
        QueryBuilder <CropField,Integer> builder = this.queryBuilder();
                 builder.where().eq("id",id+1);
        PreparedQuery<CropField> preparedQuery =builder.prepare();
        List <CropField> list =this.query(preparedQuery);
        Log.d(TAG, String.valueOf(list.size()));

        return list.iterator().next();
    }
    public CropField getPrevious(int id) throws SQLException {
        QueryBuilder <CropField,Integer> builder = this.queryBuilder();
        builder.where().eq("id",id-1);
        PreparedQuery<CropField> preparedQuery =builder.prepare();
        List <CropField> list =this.query(preparedQuery);
        Log.d(TAG, String.valueOf(list.size()));

        return list.iterator().next();
    }
}