package com.isoftstone.smartsite.http.aqi;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.pageable.PageBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.video.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.video.DevicesBeanPage;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/10/29.
 */

public class EQIMonitoring {
    private static  String TAG = "EQIMonitoring";
    public static EQIRankingBean eqiDataRanking(String strurl, OkHttpClient mClient, String archId, String time){
        EQIRankingBean eqiRankingBean = null;
        String funName = "EQIMonitoring";
        try {
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);

            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  eqiDataRanking(strurl,mClient,archId,time);
            }
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                responsebody = responsebody.replaceAll("PM2.5","PM2_5");
                Gson gson = new Gson();
                eqiRankingBean = gson.fromJson(responsebody,EQIRankingBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  eqiRankingBean;
    }

    public static MonthlyComparisonBean carchMonthlyComparison(String strurl, OkHttpClient mClient, String archId, String time, String type){
        MonthlyComparisonBean monthlyComparisonBean = null;
        try {
            //区域月度数据对比
            String funName = "carchMonthlyComparison";
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);
            object.put("type",type);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  carchMonthlyComparison(strurl,mClient,archId,time,type);
            }
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                //
                Gson gson = new Gson();
                if(type.equals("0")){
                    ArrayList<MonthlyComparisonBean.AirQualityBean> list = new ArrayList<MonthlyComparisonBean.AirQualityBean>();
                    list = HttpPost.stringToList(responsebody,MonthlyComparisonBean.AirQualityBean.class);
                    monthlyComparisonBean = new MonthlyComparisonBean();
                    monthlyComparisonBean.setCurrentMonth(list);
                }else {
                    monthlyComparisonBean = gson.fromJson(responsebody,MonthlyComparisonBean.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return monthlyComparisonBean;
    }

    public static ArrayList<WeatherConditionBean> getWeatherConditionDay(String strurl, OkHttpClient mClient, String archId, String time){
        ArrayList<WeatherConditionBean> list = null;
        String funName = "getWeatherConditionDay";
        try {
            //优良天数占比
            JSONObject object = new JSONObject();
            object.put("archId",archId);
            object.put("time",time);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();


            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getWeatherConditionDay(strurl,mClient,archId,time);
            }
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                //
                list = HttpPost.stringToList(responsebody,WeatherConditionBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static  ArrayList<DataQueryVoBean> onePMDevicesDataList(String strurl, OkHttpClient mClient, String deviceIdsStr, String dataType, String beginTime, String endTime){
        //2.2	单设备PM数据列表
        ArrayList<DataQueryVoBean> list = null;
        String funName = "onePMDevicesDataList";
        FormBody body = new FormBody.Builder()
                .add("size",300+"")
                .add("deviceIdsStr", deviceIdsStr)
                .add("dataType",dataType)
                .add("beginTime",beginTime)
                .add("endTime",endTime)
                .build();
        Request request = new Request.Builder()
                .addHeader("X-Requested-With","X-Requested-With")
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  onePMDevicesDataList(strurl,mClient,deviceIdsStr,dataType,beginTime,endTime);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  list;
    }


    public static  DataQueryVoBeanPage onePMDevicesDataListPage(String strurl, OkHttpClient mClient, String deviceIdsStr, String dataType, String beginTime, String endTime, PageableBean pageableBean){
        //2.2	单设备PM数据列表
        DataQueryVoBeanPage dataQueryVoBeanPage = null;
        String funName = "onePMDevicesDataListPage";
        FormBody body = new FormBody.Builder()
                .add("page",pageableBean.getPage()+"")
                .add("size",pageableBean.getSize()+"")
                .add("deviceIdsStr", deviceIdsStr)
                .add("dataType",dataType)
                .add("beginTime",beginTime)
                .add("endTime",endTime)
                .build();
        Request request = new Request.Builder()
                .addHeader("X-Requested-With","X-Requested-With")
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  onePMDevicesDataListPage(strurl,mClient,deviceIdsStr,dataType,beginTime,endTime,pageableBean);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                dataQueryVoBeanPage = gson.fromJson(responsebody,DataQueryVoBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  dataQueryVoBeanPage;
    }

    public static ArrayList<DataQueryVoBean> getOneDevicesHistoryData(String strurl, OkHttpClient mClient,String id){
        //2.3	单设备PM历史数据
        ArrayList<DataQueryVoBean> list = null;
        String funName = "getOneDevicesHistoryData";
        Request request = new Request.Builder()
                .url(strurl+id)
                .get()
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getOneDevicesHistoryData(strurl,mClient,id);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                list = HttpPost.stringToList(responsebody,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static  ArrayList<DataQueryVoBean> onePMDevices24Data(String strurl, OkHttpClient mClient,String deviceId,String pushTime){
        //2.5	单设备某天24小时数据
        ArrayList<DataQueryVoBean> list = null;
        String funName = "onePMDevices24Data";
        FormBody body = new FormBody.Builder()
                .add("deviceId", deviceId)
                .add("pushTime",pushTime)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  onePMDevices24Data(strurl,mClient,deviceId,pushTime);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                list = HttpPost.stringToList(responsebody,DataQueryVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  list;
    }


    public static ArrayList<DevicesBean> getDevicesList(String strurl, OkHttpClient mClient, String deviceType, String deviceName, String archId, String deviceStatus){
        //获取设备列表—文昊炅  ESS_DEVICE_LIST
        ArrayList<DevicesBean> list = null;
        String funName = "getDevicesList";
        FormBody  body = new FormBody.Builder()
                .add("deviceType", deviceType)
                .add("deviceName", deviceName)
                .add("archId", archId)
                .add("size",300+"")
                .add("deviceStatus", deviceStatus)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getDevicesList(strurl,mClient,deviceType,deviceName,archId,deviceStatus);
            }
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content,DevicesBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static DevicesBeanPage getDevicesListPage(String strurl, OkHttpClient mClient, String deviceType, String deviceName, String archId, String deviceStatus, PageableBean pageableBean){
        //获取设备列表—文昊炅  ESS_DEVICE_LIST
        DevicesBeanPage devicesBeanPage = null;
        String funName = "getDevicesList";
        FormBody  body = new FormBody.Builder()
                .add("deviceType", deviceType)
                .add("deviceName", deviceName)
                .add("archId", archId)
                .add("size",pageableBean.getSize())
                .add("page",pageableBean.getPage())
                .add("deviceStatus", deviceStatus)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getDevicesListPage(strurl,mClient,deviceType,deviceName,archId,deviceStatus,pageableBean);
            }
            if(response.isSuccessful()){
                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                devicesBeanPage = gson.fromJson(responsebody,DevicesBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devicesBeanPage;
    }
}
