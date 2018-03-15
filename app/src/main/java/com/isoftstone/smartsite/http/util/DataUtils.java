package com.isoftstone.smartsite.http.util;

/**
 * Created by gone on 2017/12/10.
 */

public class DataUtils {

    public static  double doubleToString(double data,int lenth){
        String returnData = data+"";
        int len = returnData.length();
        int index = returnData.indexOf(".");
        if(index > 0 && len - index - 1 > lenth){
            returnData = returnData.substring(0,index+lenth+1);
        }
        return Double.parseDouble(returnData);
    }
}
