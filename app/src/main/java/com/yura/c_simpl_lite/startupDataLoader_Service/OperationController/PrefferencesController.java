package com.yura.c_simpl_lite.startupDataLoader_Service.OperationController;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



/**
 * Created by Yuriy S on 09.07.2016.
 */
public class PrefferencesController extends OperationController {

    final String TAG = "myLog";
    final String PREFS_NAME = "MyPrefsFile";

Activity activity;
    @Override
    protected void initSwitcher() {

        Log.d(TAG, "Checking weather it is first startup....");

        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        MyPreferencesManager manager = new MyPreferencesManager(activity);

        if ((manager.loadBoolean())) {
            //the app is being launched for the first time, do something
            Log.d(TAG, "This is the first time app starts....");

            // first time start - let the swither be opened
            super.switherIsOpened = true;
            // record the fact that the app has been started at least once
            manager.saveBoolean(false);
//            Log.d(TAG, manager.loadBoolean() + "");

        }
    }

    public void loadOnFirstStartup(WorkToDo work) {
        Log.d("myLog", "loadOnFirstStartup(WorkToDo work)....");
        super.doUnderControll(work);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}