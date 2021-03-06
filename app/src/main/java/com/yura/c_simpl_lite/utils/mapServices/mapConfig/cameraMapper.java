package com.yura.c_simpl_lite.utils.mapServices.mapConfig;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yura.c_simpl_lite.utils.mapServices.myLocationService.PlayServiceConnection;

/**
 * Created by Yuriy S on 16.07.2016.
 */
public class CameraMapper implements View.OnClickListener {
    //   public final static int USE_STANDART_CURRENT_LOCATION_BUTTON = -1;
    public final static String TAG = "myConLog";
    private Settings markerSettings;
    GoogleMap map;
    LatLng destination;
    PlayServiceConnection playServiceConnection;
    String test;

    public CameraMapper(GoogleMap map, LatLng destination) {
        this.map = map;
        this.destination = destination;
        Log.d(PlayServiceConnection.TAG, "in CameraMapper constructor: destination == null: " + String.valueOf(destination == null));

    }


    public CameraMapper(GoogleMap map, PlayServiceConnection playServiceConnection) {
        this.map = map;
        this.playServiceConnection = playServiceConnection;
        this.test = "so what";
    }

    private CameraMapper setUpMarker() {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(destination);
        map.addMarker(markerOptions);
        return this;

    }


    public CameraMapper set_moveToLocationOnButtonClick(Button moveCamera) {
        set_moveToLocationOnButtonClick(moveCamera, Settings.DONT_ADD_MARKER);
        return this;
    }

    public CameraMapper set_moveToLocationOnButtonClick(Button moveCamera, Settings markerSettings) {
        this.markerSettings = markerSettings;
        moveCamera.setOnClickListener(this);
        return this;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "in onClick method, playService==null; " + String.valueOf(playServiceConnection == null));
        Log.d(TAG, "in onClick method, playService==null; " + test);
        Log.d(TAG,"marker = addMarker : "+String.valueOf(markerSettings == Settings.ADD_MARKER));
//        LatLng l=  playServiceConnection.getLastLocation_as_LatLng();
//        Log.d(TAG,l.toString());
        //            FocusedLocationProviderApi.
//        double lattitude = mLastLocation.getLatitude();
//        double longitude = mLastLocation.getLongitude();
//        LatLng myLatLng = new LatLng(lattitude,longitude);
//            activityCurrentMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        if (playServiceConnection == null) {
            //case: camera has to move back to current polygonObject
            map.animateCamera(CameraUpdateFactory.newLatLng(destination));
        } else {
            //case camera has to move to my current last location

            LatLng l = playServiceConnection.getLastLocation_as_LatLng();
            destination = l;
            Log.d(TAG, l.toString());
            map.animateCamera(CameraUpdateFactory.newLatLng(l));
        }
        if (markerSettings == Settings.ADD_MARKER) {

            setUpMarker();
        }
    }

    public enum Settings {ADD_MARKER, DONT_ADD_MARKER}
//    public CameraMapper setUpButton_HandleEvent() {
//        return this;
//
}
