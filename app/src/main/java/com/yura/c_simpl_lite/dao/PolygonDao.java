package com.yura.c_simpl_lite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.yura.c_simpl_lite.domainEntities.Polygon;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy S on 11.07.2016.
 */

public class PolygonDao extends BaseDaoImpl<Polygon, Integer> {

    public PolygonDao(ConnectionSource connectionSource,
                      Class<Polygon> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Polygon> getAllPolygons() throws SQLException {
        return this.queryForAll();
    }
}