package com.blogspot.vsvydenko.reutersreader;

import com.blogspot.vsvydenko.reutersreader.utils.PreferenceHelper;
import com.blogspot.vsvydenko.reutersreader.utils.RequestManager;

import android.app.Application;

/**
 * Created by vsvydenko on 07.10.14.
 */
public class ReutersReaderApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // RequestManager initialization
        RequestManager.getInstance(getApplicationContext());
        PreferenceHelper.init(this);
    }

}
