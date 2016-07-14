package com.yura.c_simpl_lite.domainEntities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Yuriy S on 11.07.2016.
 */

@DatabaseTable(tableName = "cropfields")
public class CropField implements Serializable{

    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String TILL_AREA = "till_area";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = "crops", canBeNull = false)
    private String crop;

    @DatabaseField(columnName = TILL_AREA, canBeNull = false)
    private double tillArea;

    @ForeignCollectionField(eager = true)
    private Collection<Polygon> multipolygon;

    public CropField() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public CropField(String name) {
        this.name = name;
    }

    public CropField(String name, String crop, double tillArea) {
        this.name = name;
        this.crop = crop;
        this.tillArea = tillArea;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public double getTillArea() {
        return tillArea;
    }

    public void setTillArea(double tillArea) {
        this.tillArea = tillArea;
    }


    public Collection<Polygon> getMultipolygon() {
        return multipolygon;
    }

    public void setMultipolygon(Collection<Polygon> multipolygon) {
        this.multipolygon = multipolygon;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return name.equals(((CropField) other).name);
    }
}