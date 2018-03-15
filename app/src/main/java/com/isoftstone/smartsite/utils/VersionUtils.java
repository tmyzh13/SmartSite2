package com.isoftstone.smartsite.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by gone on 2017/11/17.
 */

public class VersionUtils {

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static  String getVersion(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version = info.versionName;
            return "Version " + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "Version ";
        }
    }
}
