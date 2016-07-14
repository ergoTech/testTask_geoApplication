package com.yura.c_simpl_lite.controllers;

/**
 * Created by Yuriy S on 14.07.2016.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.yura.c_simpl_lite.MyCastomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;

import java.util.Collection;

public class FullScreenMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String EXTRA_KEY_FOR_COLLECTION = "keyCrF";
    Collection<CropField> cropFileds;
    private static final String TAG = "myLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Collection<CropField> cropFileds = retrieveDataFromCastomExtra();

    }



    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Collection<CropField> cropFileds = retrieveDataFromCastomExtra();
        for (CropField cropfield : cropFileds) {
            Collection<Polygon> polygons = cropfield.getMultipolygon();
            for (Polygon polygonEntity : polygons) {
                PolygonOptions polygonOptions = new PolygonOptions();
                Collection<Coordinate> coordinates = polygonEntity.getCoordinates();
                for (Coordinate coordinate :coordinates            ) {
                    LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                    Log.d(TAG, String.valueOf(coordinate.getLatitude() + " "
                            + String.valueOf(coordinate.getLongitude()) + "\n"));
                    polygonOptions.add(point);
                    builder.include(point);
                }
                polygonOptions.strokeWidth(5)
                        .strokeColor(Color.BLACK);
                com.google.android.gms.maps.model.Polygon gPOl = map.addPolygon(polygonOptions);
            }

        }
        LatLngBounds bounds = builder.build();


        LatLng center =   bounds.getCenter();



        int width = getResources().getDisplayMetrics().widthPixels;
        int heigth = getResources().getDisplayMetrics().heightPixels;
        int padding = (int)(width*0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,heigth,padding );
        map.animateCamera(cu);
        Log.d(TAG, "POLYGON HAS BEEN SETTED");


    }

    private Collection<CropField> retrieveDataFromCastomExtra() {
        MyCastomExtra.getExtrases(EXTRA_KEY_FOR_COLLECTION);
        return MyCastomExtra.getExtrases(EXTRA_KEY_FOR_COLLECTION);
    }

}