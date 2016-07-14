package com.yura.c_simpl_lite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.yura.c_simpl_lite.domainEntities.Coordinate;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy S on 11.07.2016.
 */

public class CoordinateDao extends BaseDaoImpl<Coordinate, Integer> {

    public CoordinateDao(ConnectionSource connectionSource,
                        Class<Coordinate> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Coordinate> getAllCoordinates() throws SQLException {
        return this.queryForAll();
    }
}