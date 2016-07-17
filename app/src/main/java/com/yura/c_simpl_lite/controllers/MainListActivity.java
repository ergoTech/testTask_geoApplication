package com.yura.c_simpl_lite.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.yura.c_simpl_lite.utils.castomAdapters.SimpleAnimatedAdapter;
import com.yura.c_simpl_lite.utils.staticDataHolder.MyCastomExtra;
import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.utils.startupDataLoader_Service.OperationController.PrefferencesController;
import com.yura.c_simpl_lite.utils.startupDataLoader_Service.OperationController.WorkToDo;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.utils.startupDataLoader_Service.MyDataLoader;
import com.yura.c_simpl_lite.utils.castomAdapters.MyCastomAdapter;
import com.yura.c_simpl_lite.utils.dbUtils.HelperFactory;
import com.yura.c_simpl_lite.utils.viewAddons.BaseSlideActivity;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainListActivity extends BaseSlideActivity {
    //public class MainListActivity extends MultiDexApplication implements View.OnClickListener {
    public static final String TAG = "myLog";
    MyCastomAdapter myAdapter;
    ArrayList<CropField> arrayListForAdapter;

//    int[] cropField_ids;
//    String[] cropField_names;
//    String[] cropField_crops;
//    String[] cropField_tillAreas;

    ListView lvMain;

    private  final  String EXTRA_KEY_FOR_CROPFIELD = "keyCrF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAndInitDb();
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable());
//        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
//        ContextCompat.getColor(this,R.color.action_bar_color);
        //view part of code - generates ListView
        List<CropField> cropFieldsList = null;
        try {
            cropFieldsList = HelperFactory.getHelper().getCropFieldDao().getAllCropFields();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("couldnt getAllCropFields from db");
        }
//        convertyng types LinkedList->ArrayList
        arrayListForAdapter = new ArrayList<>(cropFieldsList);
        // creating adapter
//        myAdapter = new MyCastomAdapter(this, arrayListForAdapter);
//       MyCastomAdapter myAdapter = new MyCastomAdapter(this, arrayListForAdapter);
SimpleAnimatedAdapter animAdapter = new SimpleAnimatedAdapter(this,arrayListForAdapter);
        // setting up ListView
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(animAdapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CropField selectedCropField = arrayListForAdapter.get(position);
                String msg = "position :" + position + "; cropField_id = " + selectedCropField.getId() +
                        "(" + selectedCropField.getTillArea() + ";" + selectedCropField.getName() + ")";
                Toast.makeText(MainListActivity.this, msg, Toast.LENGTH_LONG).show();
//                goToSecondActivity_useSerialization(selectedCropField);
                goToSecondActivity_useCastomExtra(selectedCropField);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(MainListActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.menuBtnMap:
                Toast.makeText(MainListActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                goToThirdActivity_useCastomExtra(arrayListForAdapter);
                break;
            case R.id.menuBtnList:
                //do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAndInitDb() {
        HelperFactory.setHelper(getApplicationContext());

        PrefferencesController controller = new PrefferencesController();
        controller.setActivity(this);
        controller.loadOnFirstStartup(new WorkToDo() {
            @Override
            public void doWork() {
                Log.e(TAG, "execution (PrefferencesController class)- doWork()");
                Log.d(TAG, "prestart load");
                MyDataLoader dl = new MyDataLoader();
                String dataToLoad = dl.getStringData(MainListActivity.this);
                try {
                    dl.populateDbWithData(dataToLoad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "DB has been created and filled with app DATA");
            }
        });
    }

    private void goToSecondActivity_useSerialization(CropField cropField) {
        Intent intent = new Intent(this, PolygonMapActivity.class);

        intent.putExtra(EXTRA_KEY_FOR_CROPFIELD, cropField);
        startActivity(intent);
    }
    private void goToSecondActivity_useParsel(){
        throw new UnsupportedOperationException();
    }

    private void goToSecondActivity_useCastomExtra(CropField cropField){
        Intent intent = new Intent(this, PolygonMapActivity.class);
        Log.d(TAG,"going to secon activity using my castom extra");
        MyCastomExtra.putExtras(EXTRA_KEY_FOR_CROPFIELD, cropField);
        startActivity(intent);

    };
    private void goToThirdActivity_useCastomExtra(Collection<CropField> CropFields){
        MyCastomExtra.putExtras(EXTRA_KEY_FOR_CROPFIELD,CropFields);
        Intent intent = new Intent(this,FullScreenMapActivity.class);
        startActivity(intent);
    }



    ////////////////////////////////////////////////////////////////////////////////////////


//    private void useArayAdapter() {
////------------------------------initializing arrays with data for arrayAdapter---------------------------------------------------------
//        Log.d(TAG, "creating data arrays for arrayAdapter ...");
//        List<CropField> cropFieldsList = null;
//        try {
//            cropFieldsList = HelperFactory.getHelper().getCropFieldDao().getAllCropFields();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("couldnt getAllCropFields from db");
//        }
//        int adapterArraysSize = cropFieldsList.size();
//        cropField_names = new String[adapterArraysSize];
//        cropField_crops = new String[adapterArraysSize];
//        cropField_tillAreas = new String[adapterArraysSize];
//
//        int positionCounter = 0;
//        for (CropField cropField : cropFieldsList) {
//            String name = cropField.getName().toString();
//            cropField_names[positionCounter] = name;
//
//            String crop = cropField.getCrop().toString();
//            cropField_crops[positionCounter] = crop;
//
//            double tillArea = cropField.getTillArea();
//            cropField_tillAreas[positionCounter] = String.valueOf(tillArea) + " Hr";
////            int id = cropField.getId();
////            Log.d(TAG, "cropField object: name= " + name + "; crop = " + crop + "; id = " + id);
//            positionCounter++;
//        }
//
////---------------------------------------------------------------------------------------------
//        // находим список
//        ListView lvMain = (ListView) findViewById(R.id.lvMain);
//        // создаем адаптер
////            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
////                    android.R.layout.simple_list_item_1, cropField_names);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.my_list_item, cropField_names);
//
//        // присваиваем адаптер списку
//        lvMain.setAdapter(adapter);
//    }

//    private void printData() {
//        Log.d(TAG, "printing...");
//        List<CropField> cropFieldsList = null;
//        try {
//            cropFieldsList = HelperFactory.getHelper().getCropFieldDao().getAllCropFields();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("couldnt getAllCropFields from db");
//        }
//        for (CropField cropField : cropFieldsList) {
//            String name = cropField.getName().toString();
//            String crop = cropField.getCrop().toString();
//            int id = cropField.getId();
//            Log.d(TAG, "cropField object: name= " + name + "; crop = " + crop + "; id = " + id);
//
//            for (Polygon polygon : cropField.getMultipolygon()) {
//                int polygon_id = polygon.getId();
//                int in_of_parent_cropfield = polygon.getCropField().getId();
//                Log.d(TAG, "----polygon object: polygon_id= " + polygon_id + "; id of parent cropField = " + in_of_parent_cropfield);
//
//                for (Coordinate coordinate : polygon.getCoordinates()) {
//                    double latitude = coordinate.getLatitude();
//                    double longitude = coordinate.getLongitude();
//                    int parent_polygon_id = coordinate.getPolygon().getId();
//                    Log.d(TAG, "---- ----coordinate object: parent_id= " + parent_polygon_id + "; latitude = " + latitude + "; longitude = " + longitude);
//
//                }
//
//            }
//        }
//    }

//    private void readWriteData() throws Exception {
//        // create an instance of CropField
//        String name = "Buzz Lightyear";
//        CropField cropField = new CropField(name);
//
//
//        // persist the cropField object to the database
//        HelperFactory.getHelper().getCropFieldDao().create(cropField);
//
//        // create an associated Polygon for the CropField
//        Polygon polygon1 = new Polygon(cropField);
//        HelperFactory.getHelper().getPolygonDao().create(polygon1);
//        //create and put coordinates:
//        Coordinate cor1 = new Coordinate(polygon1, 1, 1);
//        HelperFactory.getHelper().getCoordinateDao().create(cor1);
//        Coordinate cor2 = new Coordinate(polygon1, 2, 2);
//        HelperFactory.getHelper().getCoordinateDao().create(cor2);
//        Coordinate cor3 = new Coordinate(polygon1, 3, 3);
//        HelperFactory.getHelper().getCoordinateDao().create(cor3);
//
//
//        // create another Polygon for the CropField
//        // Buzz also bought 1 of item #785 for a price of $7.98
//
//        Polygon polygon2 = new Polygon(cropField);
//        HelperFactory.getHelper().getPolygonDao().create(polygon2);
//
//        //create and put coordinates:
//        Coordinate cor4 = new Coordinate(polygon2, 4, 4);
//        HelperFactory.getHelper().getCoordinateDao().create(cor4);
//        Coordinate cor5 = new Coordinate(polygon2, 5, 5);
//        HelperFactory.getHelper().getCoordinateDao().create(cor5);
//        Coordinate cor6 = new Coordinate(polygon2, 6, 6);
//        HelperFactory.getHelper().getCoordinateDao().create(cor6);
//        Log.d(TAG, "population has been finished...");
//
//        CropField cropFieldResult = HelperFactory.getHelper().getCropFieldDao().queryForId(cropField.getId());
//        Collection<Polygon> polygons = cropFieldResult.getMultipolygon();
//        for (Polygon polygon : polygons) {
//            polygon.getId();
//            Log.d(TAG, "polygon id =" + polygon.getId() + "parrent = " + polygon.getCropField().getName());
//            for (Coordinate c : polygon.getCoordinates()) {
//                c.getLatitude();
//                c.getLongitude();
//                Log.d(TAG, "Coordinate id =" + c.getId() + "parrent = " + c.getPolygon().getId());
//                Log.d(TAG, "[longitude =" + c.getLongitude() + "; latitude = " + c.getLatitude() + "]");
//
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

		/*
         * You'll need this in your class to release the helper when done.
		 */
        if (HelperFactory.getHelper() != null) {
            OpenHelperManager.releaseHelper();
            HelperFactory.releaseHelper();
        }
    }





//    @Override
//    public void attachBaseContext(Context base) {
//        MultiDex.install(base);
//        super.attachBaseContext(base);
//    }

}

