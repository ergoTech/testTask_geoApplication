package com.yura.c_simpl_lite.utils.inputDataReader;

import android.app.Activity;

/**
 * Created by Yuriy S on 07.07.2016.
 */
public class InternalResourcesFileReaderFactory implements ReaderFactory {
    private Activity activity;
    private int resId;

    public InternalResourcesFileReaderFactory(Activity activity, int resId) {
        this.activity = activity;
        this.resId = resId;
    }

    @Override
    public InternalResourcesFileReader create() {
        return new InternalResourcesFileReader(activity,resId);
    }
}
