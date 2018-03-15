package com.isoftstone.smartsite.http.aqi;

/**
 * Created by gone on 2017/10/29.
 */

public class WeatherConditionBean {
        private String name = "";
        private int value ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
}
