package com.yura.c_simpl_lite.utils.MyMapServices.MyLocationService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yuriy S on 16.07.2016.
 */
public class PlayServiceConnection implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final static String TAG = "myConLog";
    Location myLastLocation;
    GoogleApiClient mGoogleApiClient;
    Context ctxt;


    PlayServiceConnection(Context ctxt) {
        this.ctxt = ctxt;
        Log.d(TAG,"in PCconstructor");
        init();
    }
   protected void init(){
       if(mGoogleApiClient == null){
           mGoogleApiClient=new GoogleApiClient.Builder(ctxt)
                   .addConnectionCallbacks(this)
                   .addOnConnectionFailedListener(this)
                   .addApi(LocationServices.API)
                   .build();
           Log.d(TAG,"method init(): mGoogleApiClient == null: "+String.valueOf(mGoogleApiClient == null));
       }
   }
   public LatLng getLastLocation_as_LatLng(){
       Log.d(TAG, "getLastLocation_as_LatLng() ; LastLocation == null: "+String.valueOf(myLastLocation == null));
       double myCurrentLatitude = myLastLocation.getLatitude();
       double myCurrentLongitude = myLastLocation.getLongitude();
       LatLng result = new LatLng(myCurrentLatitude,myCurrentLongitude);
       return  result;
   }

   public void setUpMyLocationMarker(GoogleMap map){
       double myCurrentLatitude = myLastLocation.getLatitude();
       double myCurrentLongitude = myLastLocation.getLongitude();
       LatLng myCurrentLatLng = new LatLng(myCurrentLatitude,myCurrentLongitude);

   }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(ctxt, "permission problems", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "callback onConnected(); myLastLocation = null:"+(myLastLocation == null));

        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "callback onConnected(); myLastLocation = null:"+(myLastLocation == null));
        if (myLastLocation != null) {
           Log.d(TAG, "latitude =" + String.valueOf(myLastLocation.getLatitude()));
            Log.d(TAG, "longtude =" + String.valueOf(myLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        throw new UnsupportedOperationException("operation isnt supported ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() method:");

        throw new UnsupportedOperationException("operation isnt supported ");

    }

    public void playServiceConnectionOpen() {
        Log.d(TAG, "playSerConOpen():");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Log.d(TAG, "playSerConOpen():isconnected "+ String.valueOf(mGoogleApiClient.isConnected()));

        }
    };

    public void playServiceConnectionClose() {
        Log.d(TAG, "playSerConClose():");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            Log.d(TAG, "playSerConClose(): status - disconnected");
        }
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public Location getMyLastLocation() {
        return myLastLocation;
    }

    public Context getCtxt() {
        return ctxt;
    }

    public void setCtxt(Context ctxt) {
        this.ctxt = ctxt;
    }
}
