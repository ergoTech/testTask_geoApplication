package com.yura.c_simpl_lite.utils.inputDataReader;

import android.app.Activity;
import android.content.res.Resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Yuriy S on 07.07.2016.
 */
public class InternalResourcesFileReader implements DataReader {
    private Activity activity;
    private int resId;

     InternalResourcesFileReader(Activity activity, int resId) {
        this.activity = activity;
         this.resId=resId;
    }

    @Override
    public String readData() {

        Resources r = activity.getResources();

        InputStream inputStream = r.openRawResource(resId);
        String textFromRes = null;

        try {
            textFromRes = convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFromRes;
    }

    private String convertStreamToString(InputStream is) throws IOException {
//       String convertedInputStream = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
//        return convertedInputStream;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        baos.close();
        return baos.toString();
    }



}
