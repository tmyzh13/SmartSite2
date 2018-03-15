package com.isoftstone.smartsite.http.patrolinfo;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.CarInfoBean;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/11/21.
 */

public class PatrolInfoOperation {
    private static String TAG = "PatrolInfoOperation";
    public static ArrayList<ReportDataBean> getPatrolReportData(String strurl, OkHttpClient mClient,String time){
        ArrayList<ReportDataBean> list = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getPatrolReportData(strurl,mClient,time);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, ReportDataBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<UserTaskCountBean> getDepartmentUserTaskData(String strurl, OkHttpClient mClient,String time,String departmentId){
        ArrayList<UserTaskCountBean> list = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("departmentId", departmentId);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getDepartmentUserTaskData(strurl,mClient,time,departmentId);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, UserTaskCountBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static DepartmentMonthDataBean getDepartmentMonthDat(String strurl, OkHttpClient mClient,String time,String departmentId){
        DepartmentMonthDataBean departmentMonthDataBean = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("departmentId", departmentId);
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getDepartmentMonthDat(strurl,mClient,time,departmentId);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                departmentMonthDataBean = gson.fromJson(responsebody,DepartmentMonthDataBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return departmentMonthDataBean;
    }


    public static DepartmentsMonthTasks getDepartmentsMonthTasks(String strurl, OkHttpClient mClient,String time,String[] departmentIds){
        DepartmentsMonthTasks departmentsMonthTasks = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("departmentIds", new JSONArray(departmentIds));
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getDepartmentsMonthTasks(strurl,mClient,time,departmentIds);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                departmentsMonthTasks = gson.fromJson(responsebody,DepartmentsMonthTasks.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return departmentsMonthTasks;
    }

    public static DepartmentsMonthTasks getDepartmentReport(String strurl, OkHttpClient mClient,String time,String[] departmentIds){
        DepartmentsMonthTasks departmentsMonthTasks = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("departmentIds", new JSONArray(departmentIds));
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getDepartmentReport(strurl,mClient,time,departmentIds);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                departmentsMonthTasks = gson.fromJson(responsebody,DepartmentsMonthTasks.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return departmentsMonthTasks;
    }


}
