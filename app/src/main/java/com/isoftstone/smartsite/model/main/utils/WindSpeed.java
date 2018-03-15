package com.isoftstone.smartsite.model.main.utils;

/**
 * Created by gone on 2017/12/7.
 */

public class WindSpeed {
    public  static  String getWindSpeed(String windDirection,double windspeed){
        String ws = "";
        if(windspeed < 0.3 ){
           ws = "0级";
        }else if(windspeed >=0.3 && windspeed < 1.6 ){
            ws = "1级";
        }else if(windspeed >=1.6 && windspeed < 3.4 ){
            ws = "2级";
        }else if(windspeed >=3.4 && windspeed < 5.5 ){
            ws = "3级";
        }else if(windspeed >=5.5 && windspeed < 8.0 ){
            ws = "4级";
        }else if(windspeed >=8.0 && windspeed < 10.8 ){
            ws = "5级";
        }else if(windspeed >=10.8 && windspeed < 13.9 ){
            ws = "6级";
        }else if(windspeed >=13.9 && windspeed < 17.2 ){
            ws = "7级";
        }else if(windspeed >=17.2 && windspeed < 20.8 ){
            ws = "8级";
        }else if(windspeed >=20.8 && windspeed < 24.5 ){
            ws = "9级";
        }else if(windspeed >=24.5 && windspeed < 28.5 ){
            ws = "10级";
        }else if(windspeed >=28.5 && windspeed < 32.7 ){
            ws = "11级";
        }else if(windspeed >=32.7){
            ws = "12级";
        }
        return  windDirection + "风"+ ws;
    }
}
