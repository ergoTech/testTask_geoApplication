package com.yura.c_simpl_lite.startupDataLoader_Service;

import android.app.Activity;

import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.utils.inputDataReader.DataReader;
import com.yura.c_simpl_lite.utils.inputDataReader.InternalResourcesFileReaderFactory;
import com.yura.c_simpl_lite.utils.inputDataReader.ReaderFactory;

import org.json.JSONException;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public interface DataLoader {

    void populateDbWithData(String data) throws Exception;

    String getStringData(Activity activity);

}
