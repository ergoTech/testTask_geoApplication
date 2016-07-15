package com.yura.c_simpl_lite.controllers;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.yura.c_simpl_lite.utils.staticDataHolder.GlobalApplicationContext;
import com.yura.c_simpl_lite.utils.staticDataHolder.MyCastomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;
import com.yura.c_simpl_lite.utils.viewAddons.BaseSlideActivity;

import java.util.ArrayList;
import java.util.Collection;

public class PolygonMapActivity extends BaseSlideActivity implements OnMapReadyCallback {
//public class PolygonMapActivity extends AppCompatActivity {
final static int MAPTYPE_SELECTOR_DIALOG = 1;
final static int NOT_IMPLEMENTED_DIALOG = 2;
final static String MAP_TYPES[] = {"Road map","Hybrid","Satelite","Terrain"};
    final static String MAP_KEY_FOR_CONTEXT = "map key";
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

        setContentView(R.layout.polygon_map_activity);

//        cropField = deserializeDataFromIntent();

        cropField = retrieveDataFromCastomExtra();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//
        tvCrop = (TextView) findViewById(R.id.tvCrop);
        tvName = (TextView) findViewById((R.id.tvName));
        tvTillArea = (TextView) findViewById(R.id.tvTillArea);
        tvTillArea.setText(String.valueOf(cropField.getTillArea()) + "ha");
        tvName.setText(cropField.getName());
        tvCrop.setText(cropField.getCrop());

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            Toast.makeText(PolygonMapActivity.this, "Dont have permission for MyLocation", Toast.LENGTH_SHORT).show();
        }

//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);
        Coordinate c = cropField.getMultipolygon().iterator().next().getCoordinates().iterator().next();
        c.getLatitude();
        c.getLongitude();
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


            LatLng center = bounds.getCenter();

            polygonOptions.strokeWidth(5)
                    .strokeColor(Color.BLACK);
            com.google.android.gms.maps.model.Polygon gPOl = map.addPolygon(polygonOptions);
            int width = getResources().getDisplayMetrics().widthPixels;
            int heigth = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, heigth, padding);
            map.animateCamera(cu);
            Log.d(TAG, "POLYGON HAS BEEN SET");
            GoogleMap mapFromStorage = (GoogleMap) GlobalApplicationContext.getInstance().get("asd");

         // -----------------------------------------------------------------------
        //wierd way to handle with lost current state problem on displayOrientation switch, ( saving current menu settings)
            if(mapFromStorage!=null){
                Toast.makeText(PolygonMapActivity.this, "Map in the storage is not null", Toast.LENGTH_SHORT).show();
                map.setMapType(mapFromStorage.getMapType());
            }else{
                Toast.makeText(PolygonMapActivity.this, "Map in the storage is  null", Toast.LENGTH_SHORT).show();
                GlobalApplicationContext.getInstance().put("asd", map);
                GoogleMap mfap = (GoogleMap) GlobalApplicationContext.getInstance().get("asd");
                Log.d(TAG,"from context  (mfap==null) "+(mfap==null));
            }
         //   ------------------------------------------------------------------------
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_polygon_map_activity, menu);

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
            case R.id.menuSettings:
//                showDialog(MAPTYPE_SELECTOR_DIALOG); omg! again deprecated staff
                FragmentManager manager = getFragmentManager();
                SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
                Bundle bundle = new Bundle();
                ArrayList<String> mapTypeItems = new ArrayList<>();
                mapTypeItems.add("Road map");
                mapTypeItems.add("Hybrid");
                mapTypeItems.add("Satelite");
                mapTypeItems.add("Terrain");
                bundle.putStringArrayList(SingleChoiceDialogFragment.DATA, mapTypeItems);
                bundle.putInt(SingleChoiceDialogFragment.SELECTED, 0);
                dialog.setArguments(bundle);
                MyCastomExtra.putMap(map);
                dialog.show(manager,"Dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private showSelectorDialog

    private CropField deserializeDataFromIntent() {
        Intent intent = getIntent();
        CropField cropField = (CropField) intent.getSerializableExtra(EXTRA_KEY_FOR_CROPFIELD);

        return cropField;
    }

    private CropField retrieveDataFromCastomExtra() {
        MyCastomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
        return MyCastomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
    }

    public void click(View v) {
        Toast.makeText(PolygonMapActivity.this, "map type has been changed", Toast.LENGTH_SHORT).show();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
