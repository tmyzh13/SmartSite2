package com.isoftstone.smartsite.http.aqi;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/29.
 */

public class MonthlyComparisonBean {

    private ArrayList<AirQualityBean> currentMonth = null;
    private ArrayList<AirQualityBean> beforeMonth = null;

    public ArrayList<AirQualityBean> getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(ArrayList<AirQualityBean> currentMonth) {
        this.currentMonth = currentMonth;
    }

    public ArrayList<AirQualityBean> getBeforeMonth() {
        return beforeMonth;
    }

    public void setBeforeMonth(ArrayList<AirQualityBean> beforeMonth) {
        this.beforeMonth = beforeMonth;
    }

    public  static  class  AirQualityBean{
        private String pushTimeMonth = "";
        private String pushTimeOneDay = "";
        private String pm2_5 = "";
        private String co2 = "";
        private String pm10 = "";
        private String aqi = "";

        public String getPushTimeMonth() {
            return pushTimeMonth;
        }

        public void setPushTimeMonth(String pushTimeMonth) {
            this.pushTimeMonth = pushTimeMonth;
        }

        public String getPushTimeOneDay() {
            return pushTimeOneDay;
        }

        public void setPushTimeOneDay(String pushTimeOneDay) {
            this.pushTimeOneDay = pushTimeOneDay;
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

