package com.isoftstone.smartsite.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gone on 2017/11/19.
 */

public class SharedPreferencesUtils {
    private static final String SHARED_PREFERENCES_NAME = "isoftstone";
    private static final String SAVE_PASSWD = "save_passwd";
    private static final String BASE_WIDTH = "base_width";
    private static final String RECEIVE_NOTICE = "receive_notice";

    public static void updateSavePasswd(Activity activity, boolean isSave) {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SAVE_PASSWD, isSave);
        editor.commit();
    }

    public static boolean getSavePasswd(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        boolean isSave = settings.getBoolean(SAVE_PASSWD, true);
        return isSave;
    }

    public static void saveBaseWidth(Context context, float w) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(BASE_WIDTH, w);
        editor.commit();
    }

    public static  void setReceiveNotice(Context context, boolean receiveNotice){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(RECEIVE_NOTICE, receiveNotice);
        editor.commit();
    }

    public static boolean getReceiveNotice(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        boolean receiveNotice = settings.getBoolean(RECEIVE_NOTICE, false);
        return receiveNotice;
    }

    public static Float getBaseWidth(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        return settings.getFloat(BASE_WIDTH, 0);
    }

}
