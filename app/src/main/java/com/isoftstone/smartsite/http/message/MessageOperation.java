package com.isoftstone.smartsite.http.message;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.BeforeNMessageBean;
import com.isoftstone.smartsite.http.message.MessageBean;
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
import okhttp3.Response;

/**
 * Created by gone on 2017/10/29.
 */

public class MessageOperation {
    private static  String TAG = "EQIMonitoring";

    public static  ArrayList<MessageBean> getMessage(String strurl, OkHttpClient mClient, String title, String type, String status, String module){
        //获取消息列表  MESSAGE_LIST
        ArrayList<MessageBean> list = null;
        String funName = "getMessage";
        FormBody body = new FormBody.Builder()
                .add("title", title)
                .add("type", type)
                .add("status", status)
                .add("module", module)
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
                return getMessage(strurl,mClient,title,type,status,module);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = null;
                content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content,MessageBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return  list;
    }


    public static MessageBeanPage  getMessagePage(String strurl, OkHttpClient mClient, String title, String type, String status, String module, PageableBean pageableBean){
        //获取消息列表  MESSAGE_LIST
        MessageBeanPage messageBeanPage = null;
        String funName = "getMessage";
        FormBody body = new FormBody.Builder()
                .add("title", title)
                .add("type", type)
                .add("status", status)
                .add("module", module)
                .add("page",pageableBean.getPage())
                .add("size",pageableBean.getSize())
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
                return getMessagePage(strurl,mClient,title,type,status,module,pageableBean);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                String content = null;
                Gson gson = new Gson();
                messageBeanPage = gson.fromJson(responsebody,MessageBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  messageBeanPage;
    }

    public static  ResultBean readMessage(String strurl, OkHttpClient mClient, String id){
        ResultBean resultBean = null;
        String funName = "readMessage";
        FormBody body = new FormBody.Builder()
                .build();
        strurl = strurl.replace("{id}",id);
        Request request = new Request.Builder()
                .url(strurl)
                .patch(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG,funName+" response code "+response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return readMessage(strurl,mClient,id);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBean;
    }

    public static BeforeNMessageBean getBeforeNMessageList(String strurl, OkHttpClient mClient){
        BeforeNMessageBean beforeNMessageBean = null;
        String funName = "getBeforeNMessageList";
        FormBody body = new FormBody.Builder()
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
                return getBeforeNMessageList(strurl,mClient);
            }
            if(response.isSuccessful()){

                String responsebody = response.body().string();
                LogUtils.i(TAG,funName+" responsebody  "+responsebody);
                Gson gson = new Gson();
                beforeNMessageBean = gson.fromJson(responsebody,BeforeNMessageBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beforeNMessageBean;
    }
}
