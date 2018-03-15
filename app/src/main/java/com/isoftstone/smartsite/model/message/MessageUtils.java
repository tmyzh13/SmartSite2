package com.isoftstone.smartsite.model.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.isoftstone.smartsite.http.message.ExtraParamBean;
import com.isoftstone.smartsite.http.message.MessageBean;
import com.isoftstone.smartsite.model.main.ui.AirMonitoringActivity;
import com.isoftstone.smartsite.model.main.ui.MainFragment;
import com.isoftstone.smartsite.model.map.ui.ConstructionMontitoringMapActivity;
import com.isoftstone.smartsite.model.map.ui.MapTrackHistoryActivity;
import com.isoftstone.smartsite.model.tripartite.activity.CheckReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.ReadReportActivity;
import com.isoftstone.smartsite.model.tripartite.activity.ReplyReportActivity;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yanyongjun on 2017/12/15.
 */

public class MessageUtils {
    private final static String TAG = "MessageUtils";

    public static final String SEARCH_CODE_ENVIRON = "1|"; //环境检测
    public static final String SEARCH_CODE_ENVIRON_PM10_EXCEED = "1|1|";//PM10指数超标
    public static final String SEARCH_CODE_ENVIRON_PM10_LIMIT = "1|2|";//PM10上下线
    public static final String SEARCH_CODE_VEDIO = "2|";//视频监控
    public static final String SEARCH_CODE_VEDIO_OFFLINE = "2|1|";//视频监控离线
    public static final String SEARCH_CODE_THREE_PARTY = "3|"; //第三方协同工作
    public static final String SEARCH_CODE_THREE_PARTY_CHECK = "3|1|";//验收报告
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_PASS = "3|2|";//验收报告通过
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_REJECT = "3|3|";//报告退回
    public static final String SEARCH_CODE_THREE_PARTY_CHECK_REPLY = "3|4|"; //报告回复
    public static final String SEARCH_CODE_DIRTCAR = "4|"; //渣土车监控
    public static final String SEARCH_CODE_DIRTCAR_RECO = "4|1|";//人工识别
    public static final String SEARCH_CODE_DIRTCAR_ZUIZONG = "4|2|";//渣土车追踪
    public static final String SEARCH_CODE_TASK = "7|"; //第三方巡查监控
    public static final String SEARCH_CODE_TASK_1 = "7|1|";//巡查任务
    public static final String SEARCH_CODE_PLAN = "110|";//第三方施工巡查
    public static final String SEARCH_CODE_PLAN_APPROVAL = "110|1|";//审批计划
    public static final String SEARCH_CODE_PLAN_PASS = "110|2|";//计划通过
    public static final String SEARCH_CODE_PLAN_REJECT = "110|3|";//计划退回

    public static void enterActivity(Context context, MessageBean bean) {
        try {
            Log.e(TAG, "bean:" + bean);
            if (context == null || bean == null) {
                Log.e(TAG, "context == null || bean == null,return");
                return;
            }
            String searchCode = bean.getInfoType().getSearchCode();
            if (searchCode == null) {
                Log.e(TAG, "searchCode == null ,return");
            }
            switch (searchCode) {
                case SEARCH_CODE_ENVIRON://环境检测
                {
                    enterENVIRON(context);
                    break;
                }
                case SEARCH_CODE_ENVIRON_PM10_EXCEED://PM10指数超标
                {
                    enterENVIRON_PM10_EXCEED(context);
                    break;
                }
                case SEARCH_CODE_ENVIRON_PM10_LIMIT://PM10上下线
                {
                    enterENVIRON_PM10_LIMIT(context);
                    break;
                }
                case SEARCH_CODE_VEDIO://视频监控
                {
                    enterVEDIO(context);

                    break;
                }
                case SEARCH_CODE_VEDIO_OFFLINE://视频监控离线
                {
//                    enterVEDIO_OFFLINE(context);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY://第三方协同工作
                {
                    enterTHREE_PARTY(context);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK://验收报告
                {
                    int id = -1;
                    ExtraParamBean extraParamBean = (ExtraParamBean)bean.getExtra();
                    String strid = extraParamBean.getId();
                    if(strid!=null && !strid.equals("")){
                        id = Integer.parseInt(strid);
                    }
                    enterTHREE_PARTY_CHECK(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_PASS://验收报告通过
                {
                    int id = -1;
                    ExtraParamBean extraParamBean = (ExtraParamBean)bean.getExtra();
                    String strid = extraParamBean.getId();
                    if(strid!=null && !strid.equals("")){
                        id = Integer.parseInt(strid);
                    }
                    enterTHREE_PARTY_CHECK_PASS(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_REJECT://验收报告退回
                {
                    int id = -1;
                    ExtraParamBean extraParamBean = (ExtraParamBean)bean.getExtra();
                    String strid = extraParamBean.getId();
                    if(strid!=null && !strid.equals("")){
                        id = Integer.parseInt(strid);
                    }
                    enterTHREE_PARTY_CHECK_REJECT(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_REPLY://报告回复
                {
                    int id = -1;
                    ExtraParamBean extraParamBean = (ExtraParamBean)bean.getExtra();
                    String strid = extraParamBean.getId();
                    if(strid!=null && !strid.equals("")){
                        id = Integer.parseInt(strid);
                    }
                    enterTHREE_PARTY_CHECK_REPLY(context, id);
                    break;
                }
                case SEARCH_CODE_DIRTCAR://渣土车监控
                {
                    enterDIRTCAR(context);
                    break;
                }
                case SEARCH_CODE_DIRTCAR_RECO: //人工识别
                {
                    enterDIRTCAR_RECO(context);
                    break;
                }
                case SEARCH_CODE_DIRTCAR_ZUIZONG://渣土车追踪
                {
                    //之前的方法是跳转进入渣土车概览界面 现在要求进入车辆轨迹界面
//                    enterDIRTCAR_ZUIZONG(context);
                    enterCarTrackActivity(context,bean);
                    break;
                }
                case SEARCH_CODE_TASK://第三方巡查监控
                {
                    enterTASK(context);
                    break;
                }
                case SEARCH_CODE_TASK_1://巡查任务
                {
                    enterTASK_1(context,bean.getExtra());
                    break;
                }
                case SEARCH_CODE_PLAN://第三方施工巡查
                {
                    enterPLAN(context);
                    break;
                }
                case SEARCH_CODE_PLAN_APPROVAL://审批计划
                {
                    enterPLAN_APPROVAL(context);
                    break;
                }
                case SEARCH_CODE_PLAN_PASS://计划通过
                {
                    enterPLAN_PASS(context);
                    break;
                }
                case SEARCH_CODE_PLAN_REJECT://计划退回
                {
                    enterPLAN_REJECT(context);
                    break;
                }
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void enterActivity(Context context, HashMap<String, String> result) {
        try {
            String searchCode = result.get("searchCode");
            dumpHashMap(result);
            if (searchCode == null || context == null) {
                Log.e(TAG, "searchCode == null or context == null,return");
                return;
            }
            switch (searchCode) {
                case SEARCH_CODE_ENVIRON://环境检测
                {
                    enterENVIRON(context);
                    break;
                }
                case SEARCH_CODE_ENVIRON_PM10_EXCEED://PM10指数超标
                {
                    enterENVIRON_PM10_EXCEED(context);
                    break;
                }
                case SEARCH_CODE_ENVIRON_PM10_LIMIT://PM10上下线
                {
                    enterENVIRON_PM10_LIMIT(context);
                    break;
                }
                case SEARCH_CODE_VEDIO://视频监控
                {
                    enterVEDIO(context);
                    break;
                }
                case SEARCH_CODE_VEDIO_OFFLINE://视频监控离线
                {
                    enterVEDIO_OFFLINE(context);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY://第三方协同工作
                {
                    enterTHREE_PARTY(context);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK://验收报告
                {
                    int id = Integer.parseInt(result.get("infoId"));
                    enterTHREE_PARTY_CHECK(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_PASS://验收报告通过
                {
                    int id = Integer.parseInt(result.get("infoId"));
                    enterTHREE_PARTY_CHECK_PASS(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_REJECT://验收报告退回
                {
                    int id = Integer.parseInt(result.get("infoId"));
                    enterTHREE_PARTY_CHECK_REJECT(context, id);
                    break;
                }
                case SEARCH_CODE_THREE_PARTY_CHECK_REPLY://报告回复
                {
                    int id = Integer.parseInt(result.get("infoId"));
                    enterTHREE_PARTY_CHECK_REPLY(context, id);
                    break;
                }
                case SEARCH_CODE_DIRTCAR://渣土车监控
                {
                    enterDIRTCAR(context);
                    break;
                }
                case SEARCH_CODE_DIRTCAR_RECO: //人工识别
                {
                    enterDIRTCAR_RECO(context);
                    break;
                }
                case SEARCH_CODE_DIRTCAR_ZUIZONG://渣土车追踪
                {
                    enterDIRTCAR_ZUIZONG(context);
                    break;
                }
                case SEARCH_CODE_TASK://第三方巡查监控
                {
                    enterTASK(context);
                    break;
                }
                case SEARCH_CODE_TASK_1://巡查任务
                {
                    //enterTASK_1(context);
                    break;
                }
                case SEARCH_CODE_PLAN://第三方施工巡查
                {
                    enterPLAN(context);
                    break;
                }
                case SEARCH_CODE_PLAN_APPROVAL://审批计划
                {
                    enterPLAN_APPROVAL(context);
                    break;
                }
                case SEARCH_CODE_PLAN_PASS://计划通过
                {
                    enterPLAN_PASS(context);
                    break;
                }
                case SEARCH_CODE_PLAN_REJECT://计划退回
                {
                    enterPLAN_REJECT(context);
                    break;
                }
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterCarTrackActivity(Context context, MessageBean bean){
        Intent intent =new Intent(context, MapTrackHistoryActivity.class);
        String extraParam =bean.getExtraParam();
        String dateTime="";
        String licence="";
        try {
            JSONObject jsonObject=new JSONObject(extraParam);
            dateTime = (String) jsonObject.get("dataTimeDay");
            licence=(String)jsonObject.get("licence");
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("licence",licence);
        intent.putExtra("time",dateTime);
        context.startActivity(intent);
    }

    public static void dumpHashMap(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            Log.e(TAG, "dumpHashMap,parm == null");
            return;
        }
        Log.e(TAG, "dumpMsg begin------------------------------");
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Log.e(TAG, "key:" + key + " value:" + hashMap.get(key));
        }
        Log.e(TAG, "dumpMsg end ******************************");
    }

    //"1|"; //环境检测 //TODO please check it
    public static void enterENVIRON(Context context) {
        Intent i = new Intent(context, AirMonitoringActivity.class);
        context.startActivity(i);
    }

    //"1|1|";PM10指数超标//TODO please check it
    public static void enterENVIRON_PM10_EXCEED(Context context) {
        Intent i = new Intent(context, AirMonitoringActivity.class);
        context.startActivity(i);
    }

    //"1|2|";PM10上下线//TODO please check it
    public static void enterENVIRON_PM10_LIMIT(Context context) {
        Intent i = new Intent(context, AirMonitoringActivity.class);
        context.startActivity(i);
    }

    //"2|";视频监控//TODO please check it
    public static void enterVEDIO(Context context) {
        MainFragment.enterVideoMonitoring(context);
    }

    //"2|1|";视频监控离线//TODO please check it
    public static void enterVEDIO_OFFLINE(Context context) {
        MainFragment.enterVideoMonitoring(context);
    }

    //"3|"; 第三方协同工作
    public static void enterTHREE_PARTY(Context context) {
        MainFragment.enterThirdPartReport(context);
    }

    //"3|1|";验收报告
    public static void enterTHREE_PARTY_CHECK(Context context, int id) {
        if(id == -1){
            MainFragment.enterThirdPartReport(context);
        }else {
            Intent i = new Intent(context, CheckReportActivity.class);
            i.putExtra("_id", id);
            context.startActivity(i);
        }
    }

    //"3|2|";验收报告通过
    public static void enterTHREE_PARTY_CHECK_PASS(Context context, int id) {
        if(id == -1){
            MainFragment.enterThirdPartReport(context);
        }else {
            Intent i = new Intent(context, ReadReportActivity.class);
            i.putExtra("_id", id);
            context.startActivity(i);
        }
    }

    //"3|3|";报告退回
    public static void enterTHREE_PARTY_CHECK_REJECT(Context context, int id) {
        if(id == -1){
            MainFragment.enterThirdPartReport(context);
        }else {
            Intent i = new Intent(context, ReplyReportActivity.class);
            i.putExtra("_id", id);
            context.startActivity(i);
        }
    }

    //"3|4|"; 报告回复
    public static void enterTHREE_PARTY_CHECK_REPLY(Context context, int id) {
        if(id == -1){
            MainFragment.enterThirdPartReport(context);
        }else {
            Intent i = new Intent(context, ReadReportActivity.class);
            i.putExtra("_id", id);
            context.startActivity(i);
        }
    }

    //"4|";渣土车监控//TODO please check it
    public static void enterDIRTCAR(Context context) {
        MainFragment.enterDircar(context);
    }

    //"4|1|";人工识别//TODO please check it
    public static void enterDIRTCAR_RECO(Context context) {

    }

    //"4|2|";渣土车追踪//TODO please check it
    public static void enterDIRTCAR_ZUIZONG(Context context) {
        //之前方法进入的是渣土车概览界面，要求进入渣土车轨迹界面
        MainFragment.enterDircar(context);

    }

    //"7|"; 第三方巡查监控//TODO please check it
    public static void enterTASK(Context context) {
        MainFragment.enterConstructionMonitor(context);
    }

    //"7|1|";巡查任务//TODO please check it
    public static void enterTASK_1(Context context,Object bean) {
        ExtraParamBean extraParamBean = (ExtraParamBean) bean;
        String taskId = extraParamBean.getId();
        Intent intent = new Intent();
        intent.putExtra("taskId",Long.valueOf(taskId));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context,ConstructionMontitoringMapActivity.class);
        context.startActivity(intent);
    }

    //"110|";第三方施工巡查//TODO please check it
    public static void enterPLAN(Context context) {
        Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
        i.setPackage("com.isoftstone.smartsite");
        i.putExtra("tab", 2);
        context.sendBroadcast(i);
    }

    //"110|1|";审批计划//TODO please check it
    public static void enterPLAN_APPROVAL(Context context) {
        Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
        i.setPackage("com.isoftstone.smartsite");
        i.putExtra("tab", 2);
        context.sendBroadcast(i);
    }

    //"110|2|";计划通过//TODO please check it
    public static void enterPLAN_PASS(Context context) {
        Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
        i.setPackage("com.isoftstone.smartsite");
        i.putExtra("tab", 2);
        context.sendBroadcast(i);
    }

    //"110|3|";计划退回//TODO please check it
    public static void enterPLAN_REJECT(Context context) {
        Intent i = new Intent(com.isoftstone.smartsite.MainActivity.ACTION_CHANGE_TAB);
        i.setPackage("com.isoftstone.smartsite");
        i.putExtra("tab", 2);
        context.sendBroadcast(i);
    }


}
