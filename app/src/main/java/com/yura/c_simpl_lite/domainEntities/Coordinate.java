package com.yura.c_simpl_lite.domainEntities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Yuriy S on 11.07.2016.
 */

@DatabaseTable(tableName = "coordinates")
public class Coordinate implements Serializable {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "polygons_id")
    Polygon polygon;

    @DatabaseField(columnName = "latitude", canBeNull = false)
    double latitude;


    @DatabaseField(columnName = "longitude", canBeNull = false)
    double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(Polygon polygon, double latitude, double longitude) {
        this(latitude, longitude);
        this.polygon = polygon;
    }

    public Coordinate() {

    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
