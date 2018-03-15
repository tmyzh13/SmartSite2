package com.isoftstone.smartsite.http.aqi;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/30.
 */

public class WeatherLiveBean {

    private LiveAQI liveAQI;
    private ArrayList<DataTrend> dataTrend;

    public LiveAQI getLiveAQI() {
        return liveAQI;
    }

    public void setLiveAQI(LiveAQI liveAQI) {
        this.liveAQI = liveAQI;
    }

    public ArrayList<DataTrend> getDataTrend() {
        return dataTrend;
    }

    public void setDataTrend(ArrayList<DataTrend> dataTrend) {
        this.dataTrend = dataTrend;
    }

    public static class  LiveAQI{
        private String AQIlevel;
        private String measure;
        private String color;
        private String impact;
        private String AQI;
        private String pollutant;

         public String getAQIlevel() {
             return AQIlevel;
         }

         public void setAQIlevel(String AQIlevel) {
             this.AQIlevel = AQIlevel;
         }

         public String getMeasure() {
             return measure;
         }

         public void setMeasure(String measure) {
             this.measure = measure;
         }

         public String getColor() {
             return color;
         }

         public void setColor(String color) {
             this.color = color;
         }

         public String getImpact() {
             return impact;
         }

         public void setImpact(String impact) {
             this.impact = impact;
         }

         public String getAQI() {
             return AQI;
         }

         public void setAQI(String AQI) {
             this.AQI = AQI;
         }

         public String getPollutant() {
             return pollutant;
         }

         public void setPollutant(String pollutant) {
             this.pollutant = pollutant;
         }
     }

    public static class DataTrend{
         private String pushTimeHour;
         private String pm2_5;
         private String co2;
         private String pm10;
         private String aqi;

         public String getPushTimeHour() {
             return pushTimeHour;
         }

         public void setPushTimeHour(String pushTimeHour) {
             this.pushTimeHour = pushTimeHour;
         }

         public String getPm2_5() {
             return pm2_5;
         }

         public void setPm2_5(String pm2_5) {
             this.pm2_5 = pm2_5;
         }

         public String getCo2() {
             return co2;
         }

         public void setCo2(String co2) {
             this.co2 = co2;
         }

         public String getPm10() {
             return pm10;
         }

         public void setPm10(String pm10) {
             this.pm10 = pm10;
         }

         public String getAqi() {
             return aqi;
         }

         public void setAqi(String aqi) {
             this.aqi = aqi;
         }
     }
}
