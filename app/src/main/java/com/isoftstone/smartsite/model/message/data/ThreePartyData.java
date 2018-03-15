package com.isoftstone.smartsite.model.message.data;


import java.io.Serializable;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class ThreePartyData extends MsgData implements Serializable {
    //消息类型
    public final static int TYPE_DEFAULT = 0;
    public final static int TYPE_RECEIVE_REPORT = 1;//接收到一份巡查报告
    public final static int TYPE_SEND_REPORT = 2; //发送一份报告

    public ThreePartyData(String id, String time, String title, String details, int readStatus) {
        super(id, time, title, details, readStatus);
    }

    private String mName = "";

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}

