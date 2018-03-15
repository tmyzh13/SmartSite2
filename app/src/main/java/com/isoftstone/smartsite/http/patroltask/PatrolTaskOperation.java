package com.isoftstone.smartsite.http.patroltask;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.result.ResultBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;
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
 * Created by gone on 2017/11/18.
 */

public class PatrolTaskOperation {

    private static String TAG = "PatrolTaskOperation";

    public static PatrolTaskBeanPage getPatrolTaskList(String strurl, OkHttpClient mClient, long userId, String taskName, String address, String taskTimeStart, String taskTimeEnd, PageableBean pageableBean) {
        PatrolTaskBeanPage patrolTaskBeanPage = null;
        String funName = "getPatrolTaskList";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userId", userId + "");
            if (!taskName.equals("")) {
                builder.add("taskName", taskName);
            }
            if (!address.equals("")) {
                builder.add("address", address);
            }
            if (!taskTimeStart.equals("")) {
                builder.add("taskTimeStart", taskTimeStart);
            }
            if (!taskTimeEnd.equals("")) {
                builder.add("taskTimeEnd", taskTimeEnd);
            }
            builder.add("size", pageableBean.getSize());
            builder.add("page", pageableBean.getPage());
            FormBody body = builder.build();
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
                return getPatrolTaskList(strurl,mClient,userId,taskName,address,taskTimeStart,taskTimeEnd,pageableBean);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                patrolTaskBeanPage = gson.fromJson(responsebody, PatrolTaskBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolTaskBeanPage;
    }

    public static ArrayList<PatrolTaskBean> getPatrolTaskListAll(String strurl, OkHttpClient mClient, String userId, String planId, String planStatus, String taskType, String taskStatus, String taskTimeStart, String taskTimeEnd, PageableBean pageableBean) {
        ArrayList<PatrolTaskBean> list = null;
        String funName = "getPatrolTaskListAll";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if (!userId.equals("")) {
                builder.add("creator.id", userId + "");
            }
            if (!planId.equals("")) {
                builder.add("planId", planId);
            }
            if (!planStatus.equals("")) {
                builder.add("planStatus", planStatus);
            }
            if(!taskType.equals("")){
                builder.add("taskType", taskType);
            }
            if(!taskStatus.equals("")){
                builder.add("taskStatus", taskStatus);
            }
            if (!taskTimeStart.equals("")) {
                builder.add("taskTimeStart", taskTimeStart);
            }
            if (!taskTimeEnd.equals("")) {
                builder.add("taskTimeEnd", taskTimeEnd);
            }
            builder.add("size", pageableBean.getSize());
            builder.add("page", pageableBean.getPage());
            FormBody body = builder.build();
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
                return getPatrolTaskListAll(strurl,mClient,userId,planId,planStatus,taskType,taskStatus,taskTimeStart,taskTimeEnd,pageableBean);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody,PatrolTaskBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static PatrolTaskBean patrolTaskSave(String strurl, OkHttpClient mClient, PatrolTaskBean patrolTaskBean) {

        PatrolTaskBean patrolTask = null;
        String funName = "patrolTaskSave";
        try {
            if(patrolTaskBean.getTaskType() == 0){
                patrolTaskBean.setPlanStatus(1);
            }
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(HttpPost.JSON, gson.toJson(patrolTaskBean));
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
                return patrolTaskSave(strurl,mClient,patrolTaskBean);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                patrolTask = gson.fromJson(responsebody, PatrolTaskBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolTask;
    }

    public static PatrolTaskBean patrolTaskFindOne(String strurl, OkHttpClient mClient, long taskId) {
        PatrolTaskBean patrolTaskBean = null;
        String funName = "patrolTaskFindOne";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("taskId", taskId + "");
            FormBody body = builder.build();
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
                return patrolTaskFindOne(strurl,mClient,taskId);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                patrolTaskBean = gson.fromJson(responsebody, PatrolTaskBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolTaskBean;
    }


    public static ResultBean updateTaskStart(String strurl, OkHttpClient mClient, long taskId, String taskName) {
        ResultBean resultBean = null;
        String funName = "getPhontoList";
        try {
            JSONObject object = new JSONObject();
            object.put("taskId", taskId);
            object.put("taskName", taskName);
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
                return updateTaskStart(strurl,mClient,taskId,taskName);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBean;
    }


    public static ResultBean executeTask(String strurl, OkHttpClient mClient, long taskId, String taskName) {
        ResultBean resultBean = null;
        String funName = "executeTask";
        try {
            JSONObject object = new JSONObject();
            object.put("taskId", taskId);
            object.put("taskName", taskName);
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
                return executeTask(strurl,mClient,taskId,taskName);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBean;
    }

    public static ResultBean updatePatrolPositionStatus(String strurl, OkHttpClient mClient, long id, String position) {
        ResultBean resultBean = null;
        String funName = "updatePatrolPositionStatus";
        try {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("position", position);
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
                return updatePatrolPositionStatus(strurl,mClient,id,position);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBean;
    }


    public static ResultBean userTrack(String strurl, OkHttpClient mClient, long userId, long taskId, double longitude, double latitude) {
        ResultBean resultBean = null;
        String funName = "userTrack";
        try {
            JSONObject object = new JSONObject();
            object.put("userId",userId);
            object.put("taskId", taskId);
            object.put("longitude", longitude);
            object.put("latitude", latitude);
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
                return userTrack(strurl,mClient,userId,taskId,longitude,latitude);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBean;
    }

    public static ArrayList<BaseUserBean> findUserAll(String strurl, OkHttpClient mClient) {
        String funName = "findUserAll";
        ArrayList<BaseUserBean> list = null;
        try {
            Request request = new Request.Builder()
                    .url(strurl)
                    .get()
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return findUserAll(strurl,mClient);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, BaseUserBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
