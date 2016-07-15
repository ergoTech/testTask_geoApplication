package com.yura.c_simpl_lite.utils.staticDataHolder;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy S on 15.07.2016.
 */
public class GlobalApplicationContext {
    //todo or not TODO: 15.07.2016
    public final static String TAG = "myLog";
    private static GlobalApplicationContext globalContext = new GlobalApplicationContext();


    private Map<String, Object> applicationState = new HashMap<>();

    public void put(String key, Object obj) {
        Log.d(TAG,"putting map with key "+key+" "+(null==obj) );
        applicationState.put(key, obj);
        Log.d(TAG, "checkig map with key " + key + " " + (null==obj) );
    }

    public Object get(String key) {
        GoogleMap map = (GoogleMap) applicationState.get(key);
        Log.d(TAG, "rereaving map with key " + key + " " + (null==map) );
        return map;

    }


    public static GlobalApplicationContext getInstance() {
        return globalContext;
    }

    public void clear() {
        applicationState.clear();
    }

    //   TODO: 15.07.2016  save to JSON file
    public void saveContext() {
        throw new UnsupportedOperationException();
    }

    ;

    // TODO: 15.07.2016  save to JSON file
    public void loadContext() {
        throw new UnsupportedOperationException();
    }
}
