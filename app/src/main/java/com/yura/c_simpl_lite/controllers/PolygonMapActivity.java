package com.yura.c_simpl_lite.controllers;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.yura.c_simpl_lite.utils.mapServices.mapConfig.CameraMapper;
import com.yura.c_simpl_lite.utils.mapServices.polygonCreator.PolygonMapper;
import com.yura.c_simpl_lite.utils.mapServices.myLocationService.MyLocationManager;
import com.yura.c_simpl_lite.utils.mapServices.myLocationService.PlayServiceConnection;
import com.yura.c_simpl_lite.utils.dbUtils.HelperFactory;
import com.yura.c_simpl_lite.utils.staticDataHolder.GlobalApplicationContext;
import com.yura.c_simpl_lite.utils.staticDataHolder.MyCustomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.utils.activityAnimation.BaseSlideActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class PolygonMapActivity extends BaseSlideActivity implements OnMapReadyCallback, View.OnClickListener {


    TextView tvName;
    TextView tvCrop;
    TextView tvTillArea;

    Button btnNext;
    Button btnPrevious;
    private static final String TAG = "myLog";
    private final String EXTRA_KEY_FOR_CROPFIELD = "keyCrF";


    com.google.android.gms.maps.model.Polygon currentP;
    PlayServiceConnection connection;
    LatLng myLastLocation;
    LatLng polygonCenter;
    SupportMapFragment mapFragment;
    GoogleMap map;
    CropField cropField;
    com.google.android.gms.maps.model.Polygon nextMappedPolygon;

    public final static String TAGC = "myConLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.polygon_map_activity);
        connection = MyLocationManager.getPlayServiceConnection(this);
        Log.d(TAGC, "in onCreate connecion == null: " + String.valueOf(connection == null));
//   cropField = deserializeDataFromIntent();


        cropField = retrieveDataFromCastomExtra();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        screenInitWithData(cropField);


    }

    private void screenInitWithData(CropField currentCropField) {
        cropField = currentCropField;
        setTextToViews(currentCropField);
        btnNext = (Button) findViewById(R.id.btnNext_polygonMap);
        btnPrevious = (Button) findViewById(R.id.btnPrevious_polygonMap);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

    }

    private void setTextToViews(CropField cropField) {
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
//        map.setMyLocationEnabled(true);
        Log.d(PlayServiceConnection.TAG, "in method onMap ready");
        Log.d(TAGC, "in method onMap ready, connecion == null: " + String.valueOf(connection == null));
        Log.d(TAGC, "is connected: " + String.valueOf(connection.getmGoogleApiClient().isConnected()));


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
//        } else {
//            Toast.makeText(PolygonMapActivity.this, "Dont have permission for MyLocation", Toast.LENGTH_SHORT).show();
//        }

        map.getUiSettings().setZoomControlsEnabled(true);

        initMap(map);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    private void initMap(GoogleMap map) {
        Button moveToMyLocationButton = (Button) findViewById(R.id.btnMyLocation_polygon);

        new CameraMapper(map, connection).set_moveToLocationOnButtonClick(moveToMyLocationButton, CameraMapper.Settings.ADD_MARKER);
//       --------------------------------------------------------------------------------------
        PolygonMapper polygonMapper = new PolygonMapper(cropField, map);
        nextMappedPolygon = polygonMapper.extractFromCropField_toGoogleMap();
        LatLngBounds bounds = polygonMapper.getBounds();
        polygonCenter = bounds.getCenter();
//


        int width = getResources().getDisplayMetrics().widthPixels;
        int heigth = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, heigth, padding);
        map.animateCamera(cu);
//            if(currentP!=null) {
//                currentP.remove();
//            }
//            if(currentP!=null){
//                currentP.remove();
//            }
        currentP = nextMappedPolygon;
        Log.d(TAG, "POLYGON HAS BEEN SET");
        GoogleMap mapFromStorage = (GoogleMap) GlobalApplicationContext.getInstance().get("full_screen_map");

        // -----------------------------------------------------------------------
        //wierd way to handle with lost current state problem on displayOrientation switch, ( saving current menu settings)
        if (mapFromStorage != null) {
//                Toast.makeText(PolygonMapActivity.this, "Map in the storage is not null", Toast.LENGTH_SHORT).show();
            map.setMapType(mapFromStorage.getMapType());
        } else {
//                Toast.makeText(PolygonMapActivity.this, "Map in the storage is  null", Toast.LENGTH_SHORT).show();
            GlobalApplicationContext.getInstance().put("full_screen_map", map);
            GoogleMap mfap = (GoogleMap) GlobalApplicationContext.getInstance().get("full_screen_map");
            Log.d(TAG, "from context  (mfap==null) " + (mfap == null));
        }
        //   ------------------------------------------------------------------------

        Button moveToFieldButton = (Button) findViewById(R.id.btnField_polygon);
        new CameraMapper(map, polygonCenter).set_moveToLocationOnButtonClick(moveToFieldButton, CameraMapper.Settings.DONT_ADD_MARKER);
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
                MyCustomExtra.putMap(map);
                dialog.show(manager, "Dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private CropField deserializeDataFromIntent() {
        Intent intent = getIntent();
        CropField cropField = (CropField) intent.getSerializableExtra(EXTRA_KEY_FOR_CROPFIELD);

        return cropField;
    }

    private CropField retrieveDataFromCastomExtra() {
        MyCustomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
        return MyCustomExtra.getExtras(EXTRA_KEY_FOR_CROPFIELD);
    }


    @Override
    protected void onStop() {
        Log.d(PlayServiceConnection.TAG, "onStop() method");
        connection.playServiceConnectionClose();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(PlayServiceConnection.TAG, "onStart() method");
        connection.playServiceConnectionOpen();
        Log.d(PlayServiceConnection.TAG, "is connecnted" + String.valueOf(connection.getmGoogleApiClient().isConnected()));
    }

    @Override
    public void onClick(View v) {
        int id = cropField.getId();
//        map.clear();
        switch (v.getId()) {
            case R.id.btnNext_polygonMap:
//                Toast.makeText(PolygonMapActivity.this, "NEXT was pressed", Toast.LENGTH_SHORT).show();

                try {
                    CropField nextCropfield = HelperFactory.getHelper().getCropFieldDao().getNext(id);
                    Log.d("orm", nextCropfield.getName());
                    cropField = nextCropfield;
                } catch (NoSuchElementException e) {
                    Toast.makeText(PolygonMapActivity.this, "There are no more fields in the list", Toast.LENGTH_SHORT).show();
                    break;

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("something went wrong");
                }
                setTextToViews(cropField);
                initMap(map);
                break;
            case R.id.btnPrevious_polygonMap:

                try {
                    CropField nextCropfield = HelperFactory.getHelper().getCropFieldDao().getPrevious(id);
                    Log.d("orm", nextCropfield.getName());
                    cropField = nextCropfield;
                } catch (NoSuchElementException e) {
                    Toast.makeText(PolygonMapActivity.this, "There are no more elems at the start", Toast.LENGTH_SHORT).show();
                    break;

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("something went wrong");
                }
                setTextToViews(cropField);
                initMap(map);
                break;
        }
    }
}
