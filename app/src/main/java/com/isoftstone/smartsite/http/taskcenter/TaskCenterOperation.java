package com.isoftstone.smartsite.http.taskcenter;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.EvidencePhotoBeanPage;
import com.isoftstone.smartsite.utils.LogUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gone on 2017/11/18.
 */

public class TaskCenterOperation {
    private static String TAG = "TaskCenterOperation";

    public  static TaskNumberBean queryPendingPlan(String strurl, OkHttpClient mClient){
        TaskNumberBean taskNumberBean = null;
        String funName = "queryPendingPlan";
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
                return queryPendingPlan(strurl,mClient);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                taskNumberBean = gson.fromJson(responsebody,TaskNumberBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskNumberBean;
    }
}
