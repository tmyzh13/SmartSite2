package com.isoftstone.smartsite.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by zw on 2017/10/11.
 * Applicaton管理类，管理Activity
 */

public class AppManager {

    private static Stack<Activity> mActivities;
    private static AppManager mInstance;

    private AppManager(){}

    public static AppManager getAppManager(){
        if(mInstance == null){
            synchronized (AppManager.class){
                if(mInstance == null){
                    mInstance = new AppManager();
                    mActivities = new Stack<>();
                }
            }
        }
        return mInstance;
    }

    public void addToActivities(Activity activity){
        mActivities.add(activity);
    }

    public void removeFromActivities(Activity activity){
        mActivities.remove(activity);
    }

    public void finishCurrentActivity(){
        Activity activity = mActivities.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity){
        if(activity != null){
            mActivities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishAllActivity(){
        while (mActivities.lastElement() != null){
            Activity activity = mActivities.lastElement();
            mActivities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

}
