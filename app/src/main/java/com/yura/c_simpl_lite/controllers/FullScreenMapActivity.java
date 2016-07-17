package com.yura.c_simpl_lite.controllers;

/**
 * Created by Yuriy S on 14.07.2016.
 */

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.yura.c_simpl_lite.utils.MyMapServices.MapConfig.Mapper;
import com.yura.c_simpl_lite.utils.MyMapServices.MyLocationService.MyLocationManager;
import com.yura.c_simpl_lite.utils.MyMapServices.MyLocationService.PlayServiceConnection;
import com.yura.c_simpl_lite.utils.staticDataHolder.GlobalApplicationContext;
import com.yura.c_simpl_lite.utils.staticDataHolder.MyCastomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;
import com.yura.c_simpl_lite.utils.viewAddons.BaseSlideActivity;

import java.util.Collection;

//public class FullScreenMapActivity extends FragmentActivity implements OnMapReadyCallback {
public class FullScreenMapActivity extends BaseSlideActivity implements OnMapReadyCallback {
    private final String EXTRA_KEY_FOR_COLLECTION = "keyCrF";
    Collection<CropField> cropFileds;
    private static final String TAG = "myLog";
    GoogleApiClient mGoogleApiClient;
    PlayServiceConnection connection;
    LatLng myLastLocation;
    LatLng polygonCenter;
    GoogleMap activityCurrentMap;
    String mLatitudeText;
    String mLongitudeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.full_screen_map);
        setContentView(R.layout.full_screen_map);
//        hideBar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Collection<CropField> cropFileds = retrieveDataFromCastomExtra();


        connection = MyLocationManager.getPlayServiceConnection(this);
//        if (mGoogleApiClient == null) {
//            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
//            // See https://g.co/AppIndexing/AndroidStudio for more information.
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API).build();
//            ;
////            .addApi(AppIndex.API)
//        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        activityCurrentMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(FullScreenMapActivity.this, "need permission for gps", Toast.LENGTH_SHORT).show();

            return;
        }
//        map.setMyLocationEnabled(true);
//        map.setOnMyLocationButtonClickListener(myLocetionListener);
        Button moveToMyLocationButton = (Button) findViewById(R.id.btnMyLocation_fullscreen);
//        Button moveToFieldButton = (Button) findViewById(R.id.btnPmoveToPolygon_polygonMap);
        new Mapper(map, connection).set_moveToLocationOnButtonClick(moveToMyLocationButton, Mapper.Settings.ADD_MARKER);


        GoogleMap savedMap = (GoogleMap) GlobalApplicationContext.getInstance().get("full_screen_map");
        if (savedMap != null) {
            map.setMapType(savedMap.getMapType());
        }
        // Add a marker in Sydney, Australia, and move the camera.
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Collection<CropField> cropFileds = retrieveDataFromCastomExtra();
        for (CropField cropfield : cropFileds) {
            Collection<Polygon> polygons = cropfield.getMultipolygon();
            for (Polygon polygonEntity : polygons) {
                PolygonOptions polygonOptions = new PolygonOptions();
                Collection<Coordinate> coordinates = polygonEntity.getCoordinates();
                for (Coordinate coordinate : coordinates) {
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





        int width = getResources().getDisplayMetrics().widthPixels;
        int heigth = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, heigth, padding);
        map.animateCamera(cu);
        Log.d(TAG, "POLYGON HAS BEEN SET");
        LatLng center = bounds.getCenter();
        Button moveToFieldButton = (Button) findViewById(R.id.btnField_fullscreen);
        new Mapper(map, center).set_moveToLocationOnButtonClick(moveToFieldButton, Mapper.Settings.DONT_ADD_MARKER);
    }

    private Collection<CropField> retrieveDataFromCastomExtra() {
        MyCastomExtra.getExtrases(EXTRA_KEY_FOR_COLLECTION);
        return MyCastomExtra.getExtrases(EXTRA_KEY_FOR_COLLECTION);
    }

//    private GoogleMap.OnMyLocationButtonClickListener myLocetionListener = new GoogleMap.OnMyLocationButtonClickListener() {
//        @Override
//        public boolean onMyLocationButtonClick() {
////            FocusedLocationProviderApi.
//            double lattitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//            LatLng myLatLng = new LatLng(lattitude, longitude);
////            activityCurrentMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
//            activityCurrentMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
//
//
//            activityCurrentMap.addMarker(new MarkerOptions().position(myLatLng));
//            return true;
//        }
//    };

//    @Override

//    public void onConnected(Bundle connectionHint) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//            Log.d(TAG, "Location is not null");
//            Log.d(PlayServiceConnection.TAG, "Location is not null");
//        }
//    }




//    @Override
//    public void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//
//        mGoogleApiClient.disconnect();
//    }

    private void hideBar() {
        View decorView = getWindow().getDecorView();
        //hide the status bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();


    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(PlayServiceConnection.TAG, "onStart() method");
        connection.playServiceConnectionOpen();
        Log.d(PlayServiceConnection.TAG, "is connecnted" + String.valueOf(connection.getmGoogleApiClient().isConnected()));
    }
    @Override
    protected void onStop() {
        Log.d(PlayServiceConnection.TAG, "onStop() method");
        connection.playServiceConnectionClose();
        super.onStop();
    }

}