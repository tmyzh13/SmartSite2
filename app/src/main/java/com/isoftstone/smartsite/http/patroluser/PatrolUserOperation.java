package com.isoftstone.smartsite.http.patroluser;

import android.util.Log;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/11/18.
 */

public class PatrolUserOperation {

    private static String TAG = "PatrolUserOperation";

    public  static ArrayList<UserTrackBean>  getUserTrack(String strurl, OkHttpClient mClient){
        ArrayList<UserTrackBean> list = null;
        String funName = "getUserTrack";
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
                return getUserTrack(strurl,mClient);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, UserTrackBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<UserTrackBean>  findByUserIdAndTaskId(String strurl, OkHttpClient mClient,UserTrackBean userTrackBean){
        ArrayList<UserTrackBean> list = null;
        String funName = "getUserTrack";
        try {
            JSONObject object = new JSONObject();
            object.put("taskId", userTrackBean.getTaskId());
            object.put("userId", userTrackBean.getUserId());
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
                return findByUserIdAndTaskId(strurl,mClient,userTrackBean);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, UserTrackBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
