package com.isoftstone.smartsite.http.muckcar;

/**
 * Created by gone on 2017/11/16.
 */
/*
卡口相机抓取的车辆信息
 */
public class BayonetGrabInfoBean {
    private String licence;    //车牌号
    private String dateTime;	//卡口摄像机抓取时间
    private String dataTimeDay;	//卡口摄像机抓取时间的天数
    private String addr;	    //卡口摄像机抓取地点
    private  int speed;         //卡口摄像机抓取时车速
    private String deviceCoding;//卡口编号
    private String imgList;     //抓拍图片

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDataTimeDay() {
        return dataTimeDay;
    }

    public void setDataTimeDay(String dataTimeDay) {
        this.dataTimeDay = dataTimeDay;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }
}
