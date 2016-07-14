package com.yura.c_simpl_lite.startupDataLoader_Service.OperationController;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by Yuriy S on 09.07.2016.
 */
public class MyPreferencesManager {
    final String PREFS_NAME = "MyPrefsFile";
    Activity activity;
    SharedPreferences sPref;
    final String SAVED_BOOL = "savedTextKey";

       public MyPreferencesManager(Activity activity) {
        this.activity = activity;
    }

    public void saveBoolean(boolean b) {
//        sPref = activity.getPreferences(Context.MODE_PRIVATE);
         sPref = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(SAVED_BOOL, b);
        ed.commit();

    }

    public boolean loadBoolean() {

        sPref = activity.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        boolean savedBoolean = sPref.getBoolean(SAVED_BOOL,true);
        return  savedBoolean;
    }
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
