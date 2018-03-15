package com.isoftstone.smartsite.http.video;

/**
 * Created by gone on 2017/10/17.
 * modifed by zhangyinfu on 2017/10/19
 * modifed by zhangyinfu on 2017/10/22
 */

public class VideoMonitorBean {
    private String resCode;
    private String resName;
    private int resType;
    private int resSubType;
    private boolean isOnline = false; //false 在线   true离线
    private boolean isShared = false; //false 不共享   true共享
    private String flieName;
    private String beginData;
    private String endData;

    public VideoMonitorBean(String beginData, String endData, String flieName, String resCode) {
        this.beginData = beginData;
        this.endData = endData;
        this.flieName =flieName;
        this.resCode = resCode;
    }

    public VideoMonitorBean(String beginData, String endData, String flieName, String resCode, int resSubType, String resName, boolean isOnline ) {
        this.beginData = beginData;
        this.endData = endData;
        this.flieName =flieName;
        this.resCode = resCode;
        this.resName = resName;
        this.resType = resSubType;
        this.isOnline = isOnline;
    }

    public VideoMonitorBean(String resCode, int resType, String resName, boolean isOnline) {
        this.resCode = resCode;
        this.resName = resName;
        this.resType = resType;
        this.isOnline = isOnline;
    }

    public VideoMonitorBean(String resCode, String resName, int resType, int resSubType, boolean isOnline, boolean isShared) {
        this.resCode = resCode;
        this.resName = resName;
        this.resType = resType;
        this.resSubType = resSubType;
        this.isOnline = isOnline;
        this.isShared = isShared;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public int getResSubType() {
        return resSubType;
    }

    public void setResSubType(int resSubType) {
        this.resSubType = resSubType;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getFlieName() {
        return flieName;
    }

    public void setFlieName(String mFlieName) {
        this.flieName = flieName;
    }

    public String getBeginData() {
        return beginData;
    }

    public void setBeginData(String beginData) {
        this.beginData = beginData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    @Override
    public String toString() {
        return "VideoMonitorBean{" +
                "resCode='" + resCode + '\'' +
                ", resName='" + resName + '\'' +
                ", resType=" + resType +
                ", resSubType=" + resSubType +
                ", isOnline=" + isOnline +
                ", isShared=" + isShared +
                '}';
    }
}
