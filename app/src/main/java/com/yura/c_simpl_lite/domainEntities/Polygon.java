package com.yura.c_simpl_lite.domainEntities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Yuriy S on 11.07.2016.
 */

@DatabaseTable(tableName = "polygons")
public class Polygon implements Serializable {

    public static final String CROPFIELD_ID_FIELD_NAME = "cropfield_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = CROPFIELD_ID_FIELD_NAME)
    private CropField cropField;



    @ForeignCollectionField(eager = true)
    private Collection<Coordinate> coordinates;



    public Polygon() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Polygon(CropField cropField) {
        this.cropField = cropField;


    }

    public int getId() {
        return id;
    }

    public CropField getCropField() {
        return cropField;
    }

    public void setCropField(CropField cropField) {
        this.cropField = cropField;
    }

    public Collection<Coordinate> getCoordinates() {
        return coordinates;
    }





}

