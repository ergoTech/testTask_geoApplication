package com.yura.c_simpl_lite.utils.mapServices.polygonCreator;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yuriy S on 18.07.2016.
 */
public class PolygonMapper {
    //    CropField currentCropField;
    Collection<com.yura.c_simpl_lite.domainEntities.Polygon> domainPolygons;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    LatLng polygonCenter;
    boolean holeMarker = false;
    GoogleMap map;
    LatLngBounds bounds;

    public PolygonMapper(CropField currentCropField, GoogleMap googleMap) {
        this.map = googleMap;
        domainPolygons = currentCropField.getMultipolygon();

    }

    public Polygon extractFromCropField_toGoogleMap() {

        PolygonOptions polygonOptions = new PolygonOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (com.yura.c_simpl_lite.domainEntities.Polygon p : domainPolygons) {

            Collection<Coordinate> coordinates = p.getCoordinates();
            List<LatLng> holes = null;
            if (holeMarker) {
                holes = new LinkedList<>();
            }
            for (Coordinate coordinate :
                    coordinates) {
                LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());


                if (holeMarker) {
                    holes.add(point);
                } else {
                    //outer geometry
                    polygonOptions.add(point);
                    builder.include(point);
                }

            }
            if(holeMarker&&holes!=null){
                polygonOptions.addHole(holes);
            }

            holeMarker = true;
        }
        bounds = builder.build();
        polygonCenter = bounds.getCenter();
        polygonOptions.strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(Color.LTGRAY);

        return map.addPolygon(polygonOptions);
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public LatLng getPolygonCenter() {
        return polygonCenter;
    }
}
