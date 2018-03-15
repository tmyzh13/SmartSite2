package com.isoftstone.smartsite.http.muckcar;

/**
 * Created by gone on 2017/11/16.
 */

public class McFlowBean {
    private  long flow;          //车流量
    private String dataTimeDay; //统计日期
    private String deviceCoding;   //统计数据来源的设备
    private String archName;    //区域名称


    public long getFlow() {
        return flow;
    }

    public void setFlow(long flow) {
        this.flow = flow;
    }

    public String getDataTimeDay() {
        return dataTimeDay;
    }

    public void setDataTimeDay(String dataTimeDay) {
        this.dataTimeDay = dataTimeDay;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public String getArchName() {
        return archName;
    }

    public void setArchName(String archName) {
        this.archName = archName;
    }

}
