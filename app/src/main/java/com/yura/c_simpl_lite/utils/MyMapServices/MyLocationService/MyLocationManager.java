package com.yura.c_simpl_lite.utils.MyMapServices.MyLocationService;

import android.content.Context;
import android.util.Log;

/**
 * Created by Yuriy S on 16.07.2016.
 */
public class MyLocationManager {

    public static PlayServiceConnection getPlayServiceConnection(Context context){
      PlayServiceConnection ps =  new PlayServiceConnection(context);
        Log.d(PlayServiceConnection.TAG,"in getPlayServConnecrion():  ps == null:"+ String.valueOf(ps == null));
        return ps;
    }
}
