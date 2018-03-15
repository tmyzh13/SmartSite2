package com.isoftstone.smartsite.utils;

import android.util.Log;

import com.isoftstone.smartsite.http.message.MessageBean;
import com.isoftstone.smartsite.model.message.data.MsgData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/11/2.
 */

public class MsgUtils {
    private final static String TAG = "msgUtils";

    public static ArrayList<MsgData> toMsgData(ArrayList<MsgData> result, ArrayList<MessageBean> list) {
        if (list == null) {
            return result;
        }
        result.clear();
        Log.e(TAG, "list size:" + list.size());
        MsgData curData = null;
        MsgData lastData = null;
        for (MessageBean msg : list) {
            lastData = curData;
            curData = new MsgData(msg.getInfoId(), msg.getUpdateTime(), msg.getTitle(), msg.getContent(), msg.getStatus());
            if (lastData != null && !curData.isSameYear(lastData)) {
                MsgData temp = new MsgData(MsgData.TYPE_YEAR);
                temp.setDataString(curData.getYear());
                result.add(temp);
            }
            Log.e(TAG, "msgData:" + curData);
            result.add(curData);
        }
        return result;
    }

    public static ArrayList<MsgData> toMsgData(ArrayList<MessageBean> list) {
        ArrayList<MsgData> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        result.clear();
        Log.e(TAG, "list size:" + list.size());
        MsgData curData = null;
        MsgData lastData = null;
        for (MessageBean msg : list) {
            lastData = curData;
            curData = new MsgData(msg.getInfoId(), msg.getUpdateTime(), msg.getTitle(), msg.getContent(), msg.getStatus());
            if (lastData != null && !curData.isSameYear(lastData)) {
                MsgData temp = new MsgData(MsgData.TYPE_YEAR);
                temp.setDataString(curData.getYear());
                result.add(temp);
            }
            Log.e(TAG, "msgData:" + curData);
            result.add(curData);
        }
        return result;
    }

    public static MsgData toMsgData(MessageBean msg) {
        if (msg == null) {
            return null;
        }
        MsgData msgData = new MsgData(msg.getInfoId(), msg.getUpdateTime(), msg.getTitle(), msg.getContent(), msg.getStatus());
        return msgData;
    }

    public static Date unFormatDate(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
