package com.blogspot.vsvydenko.reutersreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vsvydenko on 08.10.14.
 */
public class PreferenceHelper {

    static Context mContext = null;
    static SharedPreferences prefs = null;

    public static final String LAST_VISITED_FEED_TITLE = "LAST_VISITED_FEED_TITLE";


    private PreferenceHelper() {}

    public static void init(Context applicationContext) {
        if (mContext != null) {
            return;
        }

        mContext = applicationContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    private static void updatePref(String key, String value) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void setLastVisitedFeed(String title){
        updatePref(LAST_VISITED_FEED_TITLE, title);
    }

    public static String getLastVisitedFeedTitle(){
        return prefs.getString(LAST_VISITED_FEED_TITLE,"");
    }

}
