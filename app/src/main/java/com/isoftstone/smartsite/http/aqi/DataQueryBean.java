package com.isoftstone.smartsite.http.aqi;

import com.isoftstone.smartsite.http.util.DataUtils;

/**
 * Created by gone on 2017/11/1.
 */

public class DataQueryBean {
    private 	Integer  id;
    private 	Integer 	deviceId;//设备Id
    private    Integer[] deviceIds;// 	要查询的设备Id数组
    private    Integer[] detectionItemIds;//检测值类型：  0:空气温度, 1:空气湿度,2:光照强度,3:大气压力,4:风速,5:风向,
                                          // 6:雨量,7:PM2.5,8:紫外线,9:土壤温度,10:土壤湿度,11:二氧化碳
                                          //* 12:有效辐射,13:表面温度,14:PM10
    private  Integer dataType;  //数据类型：0代表实时数据，1代表5分钟查询，2代表小时查询，3代表天查询，4代表月查询
    private  String  beginTime;  //	Date	开始时间
    private String endTime;       //Date	结束时间
    private String pushTime;//	Date 	格式：yyyy-MM-dd HH:mm:ss
    private  String pushTimeMonth;//	推送月
    private double airTemperature;// 	空气温度
    private double airHumidity;// 	空气湿度
    private double lightIntensity;//	Double 	光照强度
    private double atmosphericPressure;//	Double	大气压力
    private String windDirection = "";  //风向
    private double windSpeed;     //风速

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer[] getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Integer[] deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Integer[] getDetectionItemIds() {
        return detectionItemIds;
    }

    public void setDetectionItemIds(Integer[] detectionItemIds) {
        this.detectionItemIds = detectionItemIds;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getPushTimeMonth() {
        return pushTimeMonth;
    }

    public void setPushTimeMonth(String pushTimeMonth) {
        this.pushTimeMonth = pushTimeMonth;
    }

    public double getAirTemperature() {
        return DataUtils.doubleToString(airTemperature,1);
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public double getAirHumidity() {
        return DataUtils.doubleToString(airHumidity,2);
    }

    public void setAirHumidity(double airHumidity) {
        this.airHumidity = airHumidity;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public double getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(double atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }
}
