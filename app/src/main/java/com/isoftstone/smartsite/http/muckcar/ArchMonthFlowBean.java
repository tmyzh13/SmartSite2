package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.muckcar.McFlowBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/16.
 */

public class ArchMonthFlowBean {
    private ArrayList<String> date;   //当前区域有渣土车经过的日期
    private ArrayList<McFlowBean> countFlow;//当前区域月度每日所有渣土车流量
    private ArrayList<McFlowBean> isAlarms;//当前区域月度每日黑名单渣土车流量
    private ArrayList<ArrayList<McFlowBean>> mcFlows; //不同区域月度每日渣土车流量集合

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<McFlowBean> getCountFlow() {
        return countFlow;
    }

    public void setCountFlow(ArrayList<McFlowBean> countFlow) {
        this.countFlow = countFlow;
    }

    public ArrayList<McFlowBean> getIsAlarms() {
        return isAlarms;
    }

    public void setIsAlarms(ArrayList<McFlowBean> isAlarms) {
        this.isAlarms = isAlarms;
    }

    public ArrayList<ArrayList<McFlowBean>> getMcFlows() {
        return mcFlows;
    }

    public void setMcFlows(ArrayList<ArrayList<McFlowBean>> mcFlows) {
        this.mcFlows = mcFlows;
    }
}
