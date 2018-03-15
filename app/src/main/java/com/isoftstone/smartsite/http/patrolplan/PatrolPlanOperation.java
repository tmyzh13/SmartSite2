package com.isoftstone.smartsite.http.patrolplan;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.BayonetGrabInfoBeanPage;
import com.isoftstone.smartsite.http.muckcar.EvidencePhotoBean;
import com.isoftstone.smartsite.http.pageable.PageBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.result.ResultBean;
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

public class PatrolPlanOperation {
    private static String TAG = "PatrolPlanOperation";
    public  static PatrolPlanBeanPage getPlanPaging(String strurl, OkHttpClient mClient, PatrolPlanBean patrolPlanBean, PageableBean pageableBean,String sort){
        String funName = "getPlanPaging";
        PatrolPlanBeanPage patrolPlanBeanPage = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if(patrolPlanBean.getStatus() != 0){
                builder.add("status", patrolPlanBean.getStatus()+"");
            }
            if(patrolPlanBean.getStart() != null && !patrolPlanBean.getStart().equals("")){
                builder.add("startDate", patrolPlanBean.getStart());
            }
            if(patrolPlanBean.getEndDate() != null && !patrolPlanBean.equals("")){
                builder.add("endDate", patrolPlanBean.getEndDate());
            }
            if(!sort.equals("")){
               builder.add("sort",sort);
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
                return getPlanPaging(strurl,mClient,patrolPlanBean,pageableBean,"desc");
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                patrolPlanBeanPage = gson.fromJson(responsebody,PatrolPlanBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolPlanBeanPage;
    }

    public static ResultBean  planThrough(String strurl, OkHttpClient mClient, PatrolPlanBean patrolPlanBean){
        ResultBean resultBean = null;
        String funName = "planThrough";
        try {
            JSONObject object = new JSONObject();
            object.put("id", patrolPlanBean.getId());
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .patch(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return planThrough(strurl,mClient,patrolPlanBean);
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

    public static ResultBean  planRefuse(String strurl, OkHttpClient mClient, PatrolPlanBean patrolPlanBean){
        ResultBean resultBean = null;
        String funName = "planRefuse";
        try {
            JSONObject object = new JSONObject();
            object.put("id", patrolPlanBean.getId());
            RequestBody body = RequestBody.create(HttpPost.JSON, object.toString());
            Request request = new Request.Builder()
                    .url(strurl)
                    .patch(body)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .build();
            Response response = null;
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return planRefuse(strurl,mClient,patrolPlanBean);
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

    public static  ResultBean   patrolPlanCommit(String strurl, OkHttpClient mClient, PatrolPlanCommitBean patrolPlanCommitBean){
        ResultBean resultBean = null;
        String funName = "patrolPlanCommit";
        try {
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(HttpPost.JSON, gson.toJson(patrolPlanCommitBean));
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
                return patrolPlanCommit(strurl,mClient,patrolPlanCommitBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBean;
    }
}
