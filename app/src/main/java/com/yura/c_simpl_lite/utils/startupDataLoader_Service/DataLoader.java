package com.yura.c_simpl_lite.utils.startupDataLoader_Service;

import android.app.Activity;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public interface DataLoader {

    void populateDbWithData(String data) throws Exception;

    String getStringData(Activity activity);

}
