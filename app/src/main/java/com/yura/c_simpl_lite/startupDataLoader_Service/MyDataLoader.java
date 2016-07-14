package com.yura.c_simpl_lite.startupDataLoader_Service;

import android.app.Activity;
import android.nfc.Tag;
import android.util.Log;

import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;
import com.yura.c_simpl_lite.utils.dbUtils.HelperFactory;
import com.yura.c_simpl_lite.utils.inputDataReader.DataReader;
import com.yura.c_simpl_lite.utils.inputDataReader.InternalResourcesFileReaderFactory;
import com.yura.c_simpl_lite.utils.inputDataReader.ReaderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public class MyDataLoader implements DataLoader {
    private static String TAG = "myLog";

    @Override
    public void populateDbWithData(String data) throws JSONException {
        JSONObject dataJsonObj = new JSONObject(data);
        JSONArray featuresJsonArray = dataJsonObj.getJSONArray("features");
        for (int i = 0; i < featuresJsonArray.length(); i++) {

            JSONObject featureJsonObject = featuresJsonArray.getJSONObject(i);
            JSONObject propertiesJsonObject = featureJsonObject.getJSONObject("properties");
            JSONObject geometryJsonObject = featureJsonObject.getJSONObject("geometry");

            String name = propertiesJsonObject.get("name").toString();
            String crop = propertiesJsonObject.get("crop").toString();
            double tillArea = propertiesJsonObject.getDouble("till_area");
            Log.d(TAG, " data for object : name = " + name + ", crop = " + crop + ", tillArea = " + tillArea);

            CropField cropField = new CropField(name, crop, tillArea);
            // persist the cropField object to the database
            try {
                HelperFactory.getHelper().getCropFieldDao().create(cropField);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("couldnt Persist cropField object to db");
            }
           /*     Json  scheme of geometry is represented as 4-DimensionArray :
      groupOfMultyPolygonJsonArray->multiPolygonJsonArray->polygonJsonArray->locationJsonArray[longitude,latitude];
         lets  convert it to 2-DimArray : multipolygonArray ->poligonArray->LocationObject*/
            //TODO: need to refactor. Think ab recursion method to make tree array object or look up for suit framework(Jackson or Tree Model)

            JSONArray groupOfMultyPolygonJsonArray = geometryJsonObject.getJSONArray("coordinates");
            for (int j = 0; j < groupOfMultyPolygonJsonArray.length(); j++) {
                JSONArray multiPolygonJsonArray = groupOfMultyPolygonJsonArray.getJSONArray(j);
                //do nothing -> decreasing  one dimenshion of multy dim array
                for (int k = 0; k < multiPolygonJsonArray.length(); k++) {
                    JSONArray polygonJsonArray = multiPolygonJsonArray.getJSONArray(k);


                    // create an associated Polygon for the CropField
                    //creating new polygon obj
                    Polygon polygon = new Polygon(cropField);
                    try {
                        HelperFactory.getHelper().getPolygonDao().create(polygon);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("couldnt persist Polygon object");
                    }
                    Log.d(TAG, "---------debug marker---------3");
                    for (int s = 0; s < polygonJsonArray.length(); s++) {
                        JSONArray locationJsonArray = polygonJsonArray.getJSONArray(s);
                        Log.d(TAG, "---------debug marker---------4");

                        String longitudeStr = locationJsonArray.getString(0);
                        double longitudeDbl = Double.parseDouble(longitudeStr);

                        String latitudeStr = locationJsonArray.getString(1);
                        Double latitudeDbl = Double.valueOf(latitudeStr);

                        Coordinate cordinate = new Coordinate(polygon, latitudeDbl, longitudeDbl);
                        Log.d(TAG, "---------debug marker " + String.valueOf(longitudeDbl) + " " + String.valueOf(latitudeDbl) + "---------4.1");
                        try {
                            HelperFactory.getHelper().getCoordinateDao().create(cordinate);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException("couldnt persist cordinate object");
                        }
                        Log.d(TAG, "[" + longitudeStr + ", " + latitudeStr + "]");

                    }

                }
            }
            //cropField-obj has been completely initialized;
            Log.d(TAG, "---> CropFiled Object has been created: " + cropField.toString());
        }
    }

    @Override
    public String getStringData(Activity activity) {
        ReaderFactory rf = new InternalResourcesFileReaderFactory(activity, R.raw.fields);
        DataReader internalRawFileReader = rf.create();
        String jsonString = internalRawFileReader.readData();
        return jsonString;
    }
}
