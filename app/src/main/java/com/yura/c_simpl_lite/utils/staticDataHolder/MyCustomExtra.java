package com.yura.c_simpl_lite.utils.staticDataHolder;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy S on 13.07.2016.
 */
public class MyCustomExtra {
    private  static final String TAG = "myLog";

   volatile private static Map<String,Object> myExtras = new HashMap<>();


    public static void putExtras(String key,Collection<CropField> cropFields){
        myExtras.put(key, cropFields);
    }
    public  static Collection<CropField> getExtrases(String key){
        return (Collection<CropField>) myExtras.get(key);
    }

   public static void putMap(GoogleMap map){
       myExtras.put("map",map);
   }
    public static GoogleMap getMap(){
        return (GoogleMap) myExtras.get("map");
    }
    public static void putExtras(String key,CropField cropField){

        myExtras.put(key,cropField);
        CropField retrieved = (CropField) myExtras.get(key);
        Log.d(TAG, " getting extra cropfield " + retrieved.getName() + " " + retrieved.getTillArea() + "ha;" + retrieved.getCrop());
        Log.d(TAG,"an object has been put");
    }
    public  static CropField getExtras(String key){
       CropField retrieved = (CropField) myExtras.get(key);
        Log.d(TAG," getting extra cropfield "+retrieved.getName()+" "+retrieved.getTillArea()+"ha;"+retrieved.getCrop());
        Log.d(TAG,"an object has been retrieved");
     //clean object holder map
//        myExtras.put(key,null);

        return retrieved;
    }
}
