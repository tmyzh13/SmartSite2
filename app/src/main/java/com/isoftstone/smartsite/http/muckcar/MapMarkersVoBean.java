package com.isoftstone.smartsite.http.muckcar;

/**
 * Created by gone on 2017/11/18.
 */

public class MapMarkersVoBean {
    private  String licence;	//车牌号
    private  String dateTime;	//更新时间（格式:“YYYY-MM-DD HH:mm:ss”）
    private  String addr;		//抓拍地点
    private   int speed;       	//车速
    private   String imgList;	//图片路径
    private   String installTime;	//设备安装时间（格式:“YYYY-MM-DD HH:mm:ss”）
    private   String deviceCoding;	//卡口编码(即设备编码)
    private   int deviceId;	      	//设备Id
    private   String deviceName;    //设备名称
    private   int deviceStatus;  	//设备状态：0：在线；  1：离线 ； 2：故障
    private   double latitude;	    //纬度
    private   double longitude;  	//经度

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

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MapMarkersVoBean{" +
                "licence='" + licence + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", addr='" + addr + '\'' +
                ", speed=" + speed +
                ", imgList='" + imgList + '\'' +
                ", installTime='" + installTime + '\'' +
                ", deviceCoding='" + deviceCoding + '\'' +
                ", deviceId=" + deviceId +
                ", deviceName='" + deviceName + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
