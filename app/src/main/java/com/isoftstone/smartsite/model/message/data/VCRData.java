package com.isoftstone.smartsite.model.message.data;


import java.io.Serializable;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class VCRData extends MsgData implements Serializable {
    //消息类型
    public final static int TYPE_ERROR = 1;
    public final static int TYPE_NEED_REPAIR = 2;

    public VCRData(String id, String time, String title, String details, int readStatus) {
        super(id, time, title, details, readStatus);
    }
}

