package com.isoftstone.smartsite.http.user;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/9.
 */

public class InstallBean {
    private int android_type;
    private String android_url;
    private String android_version;

    public int getAndroid_type() {
        return android_type;
    }

    public void setAndroid_type(int android_type) {
        this.android_type = android_type;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
}
