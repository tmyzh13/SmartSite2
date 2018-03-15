package com.isoftstone.smartsite.model.message.data;

import com.isoftstone.smartsite.utils.MsgUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/28.
 */

public class MsgData implements Serializable {
    public final static int TYPE_DEFAULT = 0;
    public final static int TYPE_YEAR = 3;
    public final static int TYPE_MSG = 4;

    public final static int STATUS_UNREAD = 0;
    public final static int STATUS_READ = 1;
    public final static SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
    public final static SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日");
    public final static SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat format4 = new SimpleDateFormat("yyyy");
    public final static SimpleDateFormat format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int mType = TYPE_DEFAULT;

    private String mId = null;
    private Date mTime = null;

    private String mTitle = null;
    private String mDetails = null;
    private String mDateString = null;
    private int mReadStatus = 0;

    public MsgData(String id, String time, String title, String details, int readStatus) {
        mId = id;
        mTime = MsgUtils.unFormatDate(time);
        mTitle = title;
        mDetails = details;
        mReadStatus = readStatus;
    }

    public MsgData(int type) {
        mType = type;
    }


    public void setDetails(String details) {
        mDetails = details;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getType() {
        return mType;
    }


    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public Date getTime() {
        return mTime;
    }

    public void setStatus(int status) {
        mReadStatus = status;
    }

    public int getStatus() {
        return mReadStatus;
    }

    public void setDataString(String dataString) {
        mDateString = dataString;
    }

    public String getDateString() {
        if (mDateString != null) {
            return mDateString;
        }
        Date curTime = new Date();
        if (format3.format(curTime).equals(format3.format(mTime))) {
            mDateString = format1.format(mTime);
        } else if (format3.format(curTime).equals(format3.format(new Date(mTime.getTime() + 24 * 60 * 60 * 1000)))) {
            mDateString = "昨天";
        } else {
            mDateString = format2.format(mTime);
        }
        return mDateString;
    }

    public boolean isSameYear(MsgData data) {
        return format4.format(this.getTime()).equals(format4.format(data.getTime()));
    }

    public String getYear() {
        return format4.format(getTime());
    }

    @Override
    public String toString() {
        return mId + ":" + mTime + ":" + mTitle + ":" + mDetails + ":" + mReadStatus;
    }
}
