package com.yura.c_simpl_lite.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.yura.c_simpl_lite.MyCastomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;
import com.yura.c_simpl_lite.utils.viewAddons.BaseSlideActivity;

import java.util.Collection;

public class PolygonMapActivity extends BaseSlideActivity implements OnMapReadyCallback {
//public class PolygonMapActivity extends AppCompatActivity {


    TextView tvName;
    TextView tvCrop;
    TextView tvTillArea;
    private static final String TAG = "myLog";
    private final String EXTRA_KEY_FOR_CROPFIELD = "keyCrF";

    SupportMapFragment mapFragment;
    GoogleMap map;
    CropField cropField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one_obj_elem);

//        cropField = deserializeDataFromIntent();

        cropField = retrieveDataFromCastomExtra();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//
        tvCrop = (TextView) findViewById(R.id.tvCrop);
        tvName = (TextView) findViewById((R.id.tvName));
        tvTillArea = (TextView) findViewById(R.id.tvTillArea);
        tvTillArea.setText(String.valueOf(cropField.getTillArea())+"ha");
        tvName.setText(cropField.getName());
        tvCrop.setText(cropField.getCrop());

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
        }else{
            Toast.makeText(PolygonMapActivity.this, "Dont have permission for MyLocation", Toast.LENGTH_SHORT).show();
        }

//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);
       Coordinate c = cropField.getMultipolygon().iterator().next().getCoordinates().iterator().next();
        c.getLatitude();
        c.getLongitude();
//
        initMap(map);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void initMap(GoogleMap map) {
        Collection<Polygon> polygons = cropField.getMultipolygon();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Polygon p : polygons) {
            PolygonOptions polygonOptions = new PolygonOptions();
            Collection<Coordinate> coordinates = p.getCoordinates();
//            LatLngBounds.Builder builder = new LatLngBounds.Builder(); ;
            for (Coordinate coordinate :
                    coordinates) {
                                LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                Log.d(TAG, String.valueOf(coordinate.getLatitude() + " "
                        + String.valueOf(coordinate.getLongitude()) + "\n"));
                polygonOptions.add(point);
                builder.include(point);

            }
            LatLngBounds bounds = builder.build();


          LatLng center =   bounds.getCenter();

            polygonOptions.strokeWidth(5)
                    .strokeColor(Color.BLACK);
          com.google.android.gms.maps.model.Polygon gPOl = map.addPolygon(polygonOptions);
           int width = getResources().getDisplayMetrics().widthPixels;
           int heigth = getResources().getDisplayMetrics().heightPixels;
            int padding = (int)(width*0.10);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,heigth,padding );
            map.animateCamera(cu);
            Log.d(TAG, "POLYGON HAS BEEN SETTED");

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_objectmap, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(PolygonMapActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.menuBtnGoBack:
                Toast.makeText(PolygonMapActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.menuBtnList:
                //do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private CropField deserializeDataFromIntent() {
        Intent intent = getIntent();
        CropField cropField = (CropField) intent.getSerializableExtra(EXTRA_KEY_FOR_CROPFIELD);

        return cropField;
    }
    private CropField retrieveDataFromCastomExtra(){
       MyCastomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
        return  MyCastomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
    }
}
