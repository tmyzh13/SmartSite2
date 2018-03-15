package com.isoftstone.smartsite.http.muckcar;

import android.support.constraint.solver.Goal;
import android.util.Log;

import com.google.gson.Gson;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.result.ResultBean;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
 * Created by gone on 2017/11/16.
 */

/*
渣土车信息操作类
 */
public class MuckCarOperation {
    private static String TAG = "MuckCarOperation";

    public static ArrayList<CarInfoBean> getDayFlow(String strurl, OkHttpClient mClient, String time, String parentId, String timeMonth, int flag) {
        ArrayList<CarInfoBean> list = null;
        String funName = "getDayFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("parentId", parentId);
            object.put("timeMonth", timeMonth);
            object.put("flag",flag);

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
                return  getDayFlow(strurl,mClient,time,parentId,timeMonth,flag);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, CarInfoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArchMonthFlowBean getArchMonthFlow(String strurl, OkHttpClient mClient, String time, String timeMonth, Long archId, int flag) {
        ArchMonthFlowBean archMonthFlowBean = null;
        String funName = "getArchMonthFlow";

        try {
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("flag", flag);
            object.put("timeMonth", timeMonth);
            object.put("archId", archId);

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
                return  getArchMonthFlow(strurl,mClient,time,timeMonth,archId,flag);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);

                Gson gson = new Gson();
                archMonthFlowBean = gson.fromJson(responsebody, ArchMonthFlowBean.class);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return archMonthFlowBean;
    }

    public static ArchMonthFlowBean getAlarmData(String strurl, OkHttpClient mClient, String time, String timeMonth,  long[] archIds, int flag) {
        ArchMonthFlowBean archMonthFlowBean = null;
        String funName = "getAlarmData";

        try {
            JSONArray jsonArray = new JSONArray();
            for (int i =0 ; i < archIds.length ;i ++){
                jsonArray.put(archIds[i]);
            }
            JSONObject object = new JSONObject();
            object.put("time", time);
            object.put("timeMonth", timeMonth);
            object.put("archIds", jsonArray);
            object.put("flag", flag);
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
                return getAlarmData(strurl,mClient,time,timeMonth,archIds,flag);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                archMonthFlowBean = gson.fromJson(responsebody, ArchMonthFlowBean.class);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return archMonthFlowBean;
    }


    //渣土车识别
    public static ResultBean recForMobile(String strurl, OkHttpClient mClient, String carLicence, int recResult) {
        ResultBean resultBean = null;
        String funName = "getAlarmData";
        try {
            JSONObject object = new JSONObject();
            object.put("carLicence", carLicence);
            object.put("recResult", recResult);

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
                return recForMobile(strurl,mClient,carLicence,recResult);
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


    public static BayonetGrabInfoBeanPage getUnRecList(String strurl, OkHttpClient mClient, String licence, PageableBean pageableBean) {
        BayonetGrabInfoBeanPage bayonetGrabInfoBeanPage = null;
        String funName = "getUnRecList";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if(!licence.equals("")){
                builder.add("licence", licence);
            }
            builder.add("size", pageableBean.getSize());
            builder.add("page", pageableBean.getPage());
            FormBody body =builder.build();
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
                return getUnRecList(strurl,mClient,licence,pageableBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                bayonetGrabInfoBeanPage = gson.fromJson(responsebody,BayonetGrabInfoBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bayonetGrabInfoBeanPage;
    }


    public static BayonetGrabInfoBeanPage getTrackList(String strurl, OkHttpClient mClient, String licence, PageableBean pageableBean) {
        BayonetGrabInfoBeanPage bayonetGrabInfoBeanPage = null;
        String funName = "getTrackList";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if(licence != null && !licence.equals("")){
                builder.add("licence", licence);
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
                return getTrackList(strurl,mClient,licence,pageableBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                bayonetGrabInfoBeanPage = gson.fromJson(responsebody,BayonetGrabInfoBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bayonetGrabInfoBeanPage;
    }

    public static ArrayList<EvidencePhotoBean> getPhontoList(String strurl, OkHttpClient mClient, String licence, String photoType, String takePhotoTime, String deviceCoding){
        ArrayList<EvidencePhotoBean> list = null;
        String funName = "getPhontoList";
        try {
            JSONObject object = new JSONObject();
            object.put("licence", licence);
            object.put("photoType", photoType);
            object.put("takePhotoTime", takePhotoTime);
            object.put("deviceCoding", deviceCoding);
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
                return getPhontoList(strurl,mClient,licence,photoType,takePhotoTime,deviceCoding);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, EvidencePhotoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public  static  EvidencePhotoBeanPage getEvidencePhotoList(String strurl, OkHttpClient mClient,String licence, PageableBean pageableBean){
        EvidencePhotoBeanPage evidencePhotoBeanPage = null;
        String funName = "getEvidencePhotoList";
        try {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("licence", licence);
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
                return getEvidencePhotoList(strurl,mClient,licence,pageableBean);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                Gson gson = new Gson();
                evidencePhotoBeanPage = gson.fromJson(responsebody,EvidencePhotoBeanPage.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return evidencePhotoBeanPage;
    }


    public  static  ArrayList<String>  getEvidenceDateList(String strurl, OkHttpClient mClient,String licence){
        ArrayList<String> list = null;
        String funName = "getEvidenceDateList";
        try {
            strurl = strurl + licence;
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
                return getEvidenceDateList(strurl,mClient,licence);
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

    public  static ArrayList<MapMarkersVoBean> getMapMarkers(String strurl, OkHttpClient mClient,String licence,String takePhotoTime){
        ArrayList<MapMarkersVoBean> list = null;
        String funName = "getMapMarkers";
        try {
            JSONObject object = new JSONObject();
            object.put("licence", licence);
            object.put("takePhotoTime", takePhotoTime);
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
                return getMapMarkers(strurl,mClient,licence,takePhotoTime);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                list = HttpPost.stringToList(responsebody, MapMarkersVoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String  uploadPhotos(String strurl, OkHttpClient mClient, ArrayList<String> list){
        String funName = "uploadPhotos";
        String returnString = "";
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0 ; i < list.size() ;i ++){
            File file = new File (list.get(i));
            Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
            RequestBody fileBody = RequestBody.create(MediaType.parse(mimeTypes.toString()),file);
            builder.addFormDataPart("file",file.getName(),fileBody);
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            LogUtils.i(TAG, funName + " response code " + response.code());
            if (response.code() == HttpPost.HTTP_LOGIN_TIME_OUT) {
                HttpPost.autoLogin();
                return uploadPhotos(strurl,mClient,list);
            }
            if (response.isSuccessful()) {
                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                returnString = responsebody;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString;
    }


    public  static EvidencePhotoBean  addPhoto(String strurl, OkHttpClient mClient, UpdatePhotoInfoBean updatePhotoInfoBean){
        EvidencePhotoBean evidencePhoto = null;
        String funName = "addPhoto";
        try {
            Gson gson = new Gson();
            JSONObject user = new JSONObject();
            RequestBody body = RequestBody.create(HttpPost.JSON, gson.toJson(updatePhotoInfoBean));
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
                return addPhoto(strurl,mClient,updatePhotoInfoBean);
            }
            if (response.isSuccessful()) {

                String responsebody = response.body().string();
                LogUtils.i(TAG, funName + " responsebody  " + responsebody);
                evidencePhoto = gson.fromJson(responsebody,EvidencePhotoBean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return evidencePhoto;
    }
}
