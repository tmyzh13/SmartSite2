package com.isoftstone.smartsite.http.patrolreport;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.isoftstone.smartsite.common.App;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.result.ResultBean;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import eu.medsea.mimeutil.MimeUtil;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gone on 2017/10/30.
 */

public class ReportOperation {
    private static String TAG = "ReportOperation";

    public static ArrayList<PatrolBean> getPatrolReportList(String strurl, OkHttpClient mClient, String status, String address,String departmentId, PageableBean pageableBean) {
        ArrayList<PatrolBean> list = null;
        String funName = "getPatrolList";
        FormBody body = new FormBody.Builder()
                .add("sort","date,desc")
                .add("status", "" + status)
                .add("size", "" + pageableBean.getSize())
                .add("page", "" + pageableBean.getPage())
                .add("address",""+address)
                .add("creator.departmentId", departmentId == null ? "" : departmentId)
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getPatrolReportList(strurl,mClient,status,address,departmentId,pageableBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);

                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content, PatrolBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<PatrolBean> getCheckReportList(String strurl, OkHttpClient mClient, String status, String address,PageableBean pageableBean) {
        ArrayList<PatrolBean> list = null;
        String funName = "getPatrolList";
        FormBody body = new FormBody.Builder()
                .add("sort","date,desc")
                .add("status", "" + status)
                .add("address",""+address)
                .add("size", "" + pageableBean.getSize())
                .add("page", "" + pageableBean.getPage())
                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getCheckReportList(strurl,mClient,status,address,pageableBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);

                String content = new JSONObject(responsebody).getString("content");
                list = HttpPost.stringToList(content, PatrolBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static PatrolBean addPatrolReport(String strurl, OkHttpClient mClient, PatrolBean reportBean) {

        PatrolBean patrolReportBean = null;
        String funName = "addPatrolReport";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("address", reportBean.getAddress());
        builder.add("company", reportBean.getCompany());
        String developmentCompany = reportBean.getDevelopmentCompany();
        if (developmentCompany != null && !developmentCompany.equals("")) {
            builder.add("developmentCompany", developmentCompany);
        }
        String constructionCompany = reportBean.getConstructionCompany();
        if (constructionCompany != null && !constructionCompany.equals("")) {
            builder.add("constructionCompany", constructionCompany);
        }
        String supervisionCompany = reportBean.getSupervisionCompany();
        if (supervisionCompany != null && !supervisionCompany.equals("")) {
            builder.add("supervisionCompany", supervisionCompany);
        }

        builder.add("visit", reportBean.isVisit() + "");

        String visitDate = reportBean.getVisitDate();
        if (visitDate != null && !visitDate.equals("")) {
            builder.add("visitDate", visitDate);
        }

        String category = reportBean.getCategory();
        if (!TextUtils.isEmpty(category)) {
            builder.add("category", category);
        }

        FormBody body = builder.build();
        Log.e(TAG, "addPatrolReport:" + body);
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  addPatrolReport(strurl,mClient,reportBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);

                Gson gson = new Gson();
                patrolReportBean = gson.fromJson(responsebody, PatrolBean.class);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolReportBean;
    }


    public static PatrolBean getPatrolReport(String strurl, OkHttpClient mClient, String id) {
        PatrolBean patrolReportBean = null;
        String funName = "getPatrolReport";
        Request request = new Request.Builder()
                .url(strurl + id)
                .get()
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return  getPatrolReport(strurl,mClient,id);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                patrolReportBean = gson.fromJson(responsebody, PatrolBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patrolReportBean;
    }

    public static ResultBean addPatrolVisit(String strurl, OkHttpClient mClient, ReportBean reportBean) {
        ResultBean resultBean = null;
        String funName = "addVisitReport";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(reportBean);
            LogUtils.i(TAG, "addPatrolVisit:" + json);
            RequestBody body = RequestBody.create(HttpPost.JSON, json);

            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return addPatrolVisit(strurl,mClient,reportBean);
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

    public static ResultBean addPatrolReply(String strurl, OkHttpClient mClient, ReportBean reportBean) {
        ResultBean resultBean = null;
        String funName = "addVisitReport";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(reportBean);
            RequestBody body = RequestBody.create(HttpPost.JSON, json);

            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            LogUtils.i(TAG, "addPatrolReply:" + json);
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return addPatrolReply(strurl,mClient,reportBean);
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

    public static ResultBean addPatrolCheck(String strurl, OkHttpClient mClient, ReportBean reportBean) {
        ResultBean resultBean = null;
        String funName = "addVisitReport";
        try {
            Gson gson = new Gson();
            String json = gson.toJson(reportBean);
            RequestBody body = RequestBody.create(HttpPost.JSON, json);
            LogUtils.i(TAG, "addPatrolCheck:" + json);
            Request request = new Request.Builder()
                    .url(strurl)
                    .post(body)
                    .build();
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return addPatrolCheck(strurl,mClient,reportBean);
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


    public static ResultBean reportFileUpload(String strurl, OkHttpClient mClient, String filepath, int id) {
        ResultBean resultBean = null;
        String funName = "reportFileUpload";
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        File file = new File(filepath);
        Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
        RequestBody fileBody = RequestBody.create(MediaType.parse(mimeTypes.toString()), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("id", id + "")
                .build();

        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return reportFileUpload(strurl,mClient,filepath,id);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                resultBean = gson.fromJson(responsebody,ResultBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBean;
    }

    public static long downloadfile(String downloadUrl, String storagePath, String imageName) {
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        //指定下载路径和下载文件名
        Log.i("text", storagePath);
        request.setDestinationInExternalPublicDir(storagePath, imageName);
        //获取下载管理器
        DownloadManager downloadManager = (DownloadManager) App.getAppContext().getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        return downloadManager.enqueue(request);
    }


    public static ArrayList<DictionaryBean> getDictionaryList(String strurl, OkHttpClient mClient, String lang) {
        String funName = "getDictionaryList";
        ArrayList<DictionaryBean> list = null;
        FormBody body = new FormBody.Builder()
                .add("lang", lang)
                .add("category", "patrol.category")

                .build();
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getDictionaryList(strurl,mClient,lang);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                String rawRecords = new JSONObject(responsebody).getString("rawRecords");
                list = HttpPost.stringToList(rawRecords, DictionaryBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<String> getPatrolAddress(String strurl, OkHttpClient mClient) {
        String funName = "getPatrolAddress";
        ArrayList<String> list = null;
        Request request = new Request.Builder()
                .url(strurl)
                .get()
                .addHeader("X-Requested-With", "X-Requested-With")
                .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return getPatrolAddress(strurl,mClient);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, String.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
