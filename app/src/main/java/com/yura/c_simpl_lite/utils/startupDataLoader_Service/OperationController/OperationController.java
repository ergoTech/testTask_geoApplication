package com.yura.c_simpl_lite.utils.startupDataLoader_Service.OperationController;

import android.util.Log;

/**
 * Created by Yuriy S on 09.07.2016.
 */
public abstract class OperationController {

    private  final  static String TAG = "myLog";

    boolean switherIsOpened = false;

    protected OperationController() {

    }

    protected abstract void initSwitcher();

    protected boolean doUnderControll(WorkToDo work) {
        initSwitcher();
        Log.d(TAG, "doUnderControll(WorkToDo work)....");
        if (switherIsOpened) {
            Log.d(TAG,"ahtung ahtung - switcher is opened - its FIRST STARTUP");
            action(work);
            switherIsOpened = false;
            return true;
        }else {
            Log.d(TAG," - swither is closed(false) - its NOT FIRST time STARTUP");

            return false;
        }
    }

    private void action(WorkToDo work) {

        Log.d(TAG, "action(WorkToDo work)....");
        work.doWork();
    }

    ;


}
