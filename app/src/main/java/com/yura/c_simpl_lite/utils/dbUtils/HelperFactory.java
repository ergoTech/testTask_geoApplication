package com.yura.c_simpl_lite.utils.dbUtils;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.yura.c_simpl_lite.utils.dbUtils.DatabaseHelper;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public class HelperFactory{

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getHelper(){
        return databaseHelper;
    }
    public static void setHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}
