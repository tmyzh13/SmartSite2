package com.isoftstone.smartsite.http.aqi;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by gone on 2017/11/1.
 */

public class DataQueryVoBean implements Serializable{

    private static final long serialVersionUID = 0x0011L;

    private Integer Id;
    private Integer deviceId;//设备Id
    private String deviceName;//	 	设备名称
    private String deviceCoding;//		设备编码
    private String installTime;//	Date	安装时间
    private String longitude;//		经度
    private String latitude;//		纬度
    private Integer deviceStatus;//		设备状态(0=在线  1=离线  2=故障)
    private String pushTime;//	Date 	格式：yyyy-MM-dd HH:mm:ss
    private String pushTimeMonth;//	String	推送月
    private Double airTemperature;//	Double 	空气温度
    private Double airHumidity;//	Double 	空气湿度
    private Double lightIntensity;//	Double 	光照强度
    private Double atmosphericPressure;//	Double	大气压力
    private Double windSpeed;//	Double 	风速
    private String windDirection;//	String 	风向
    private Double rainfall;//	Double 	雨量
    private Double pm2_5;//	Double 	PM2.5
    private Double uv;//	Double 	紫外线
    private Double soilTemperature;//	Double 	土壤温度
    private Double soilMoisture;//	Double	土壤湿度
    private Double co2;//	Double 	二氧化碳
    private Double radiation;//	Double 	有效辐射
    private Double surfaceTemperature;//	Double 	表面温度
    private Double pm10;//	Double 	PM10
    private Double aqiAchieveDays;//	Double	AQI达标天数
    private Double aqiVaildDays;//	Double 	AQI有效天数
    private Double aqiAchieveRate;//	Double 	AQI达标率

    private String address = "光谷一路（假数据）";

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
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

    public Double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(Double airHumidity) {
        this.airHumidity = airHumidity;
    }

    public Double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(Double lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public Double getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(Double atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        if(TextUtils.isEmpty(windDirection)){
            return "";
        }
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public Double getRainfall() {
        return rainfall;
    }

    public void setRainfall(Double rainfall) {
        this.rainfall = rainfall;
    }

    public Double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(Double pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    public Double getSoilTemperature() {
        return soilTemperature;
    }

    public void setSoilTemperature(Double soilTemperature) {
        this.soilTemperature = soilTemperature;
    }

    public Double getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public Double getCo2() {
        return co2;
    }

    public void setCo2(Double co2) {
        this.co2 = co2;
    }

    public Double getRadiation() {
        return radiation;
    }

    public void setRadiation(Double radiation) {
        this.radiation = radiation;
    }

    public Double getSurfaceTemperature() {
        return surfaceTemperature;
    }

    public void setSurfaceTemperature(Double surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getAqiAchieveDays() {
        return aqiAchieveDays;
    }

    public void setAqiAchieveDays(Double aqiAchieveDays) {
        this.aqiAchieveDays = aqiAchieveDays;
    }

    public Double getAqiVaildDays() {
        return aqiVaildDays;
    }

    public void setAqiVaildDays(Double aqiVaildDays) {
        this.aqiVaildDays = aqiVaildDays;
    }

    public Double getAqiAchieveRate() {
        return aqiAchieveRate;
    }

    public void setAqiAchieveRate(Double aqiAchieveRate) {
        this.aqiAchieveRate = aqiAchieveRate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DataQueryVoBean{" +
                "Id=" + Id +
                ", deviceId=" + deviceId +
                ", deviceName='" + deviceName + '\'' +
                ", deviceCoding='" + deviceCoding + '\'' +
                ", installTime='" + installTime + '\'' +
                ", Longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", pushTime='" + pushTime + '\'' +
                ", pushTimeMonth='" + pushTimeMonth + '\'' +
                ", airTemperature=" + airTemperature +
                ", airHumidity=" + airHumidity +
                ", lightIntensity=" + lightIntensity +
                ", atmosphericPressure=" + atmosphericPressure +
                ", windSpeed=" + windSpeed +
                ", windDirection='" + windDirection + '\'' +
                ", rainfall=" + rainfall +
                ", pm2_5=" + pm2_5 +
                ", uv=" + uv +
                ", soilTemperature=" + soilTemperature +
                ", soilMoisture=" + soilMoisture +
                ", co2=" + co2 +
                ", radiation=" + radiation +
                ", surfaceTemperature=" + surfaceTemperature +
                ", pm10=" + pm10 +
                ", aqiAchieveDays=" + aqiAchieveDays +
                ", aqiVaildDays=" + aqiVaildDays +
                ", aqiAchieveRate=" + aqiAchieveRate +
                '}';
    }
}
