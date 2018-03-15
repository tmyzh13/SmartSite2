package com.isoftstone.smartsite.model.message.data;


import java.io.Serializable;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class EnvironData extends MsgData implements Serializable {
    //消息类型

    public final static int TYPE_PM_EXTENDS = 1; //PM超标
    public final static int TYPE_NEED_REPAIR = 2; //设备需要维护

    public EnvironData(String id, String time, String title, String details, int readStatus) {
        super(id, time, title, details, readStatus);
    }
}