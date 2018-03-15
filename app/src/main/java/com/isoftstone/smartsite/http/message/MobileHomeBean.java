package com.isoftstone.smartsite.http.message;

import com.isoftstone.smartsite.http.aqi.DataQueryBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/1.
 */

public class MobileHomeBean {
    private int unreadMessages;   //未读消息总数
    private int untreatedPatrols;  //未处理报告数
    private int openVses;    //开启的视频设备数
    private int allVses;    //视频设备总数
    private int openEmes;   //开启的环境设备数
    private int allEmes;    //环境设备总数
    private int unHandleTask; //待处理任务
    private int AQI;
    private ArrayList<MessageBean> messages;  //最新N条消息
    private DataQueryBean avgEqis;        //平均环境质量指数

    public int getAQI() {
        return AQI;
    }

    public void setAQI(int AQI) {
        this.AQI = AQI;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public int getUntreatedPatrols() {
        return untreatedPatrols;
    }

    public void setUntreatedPatrols(int untreatedPatrols) {
        this.untreatedPatrols = untreatedPatrols;
    }

    public int getOpenVses() {
        return openVses;
    }

    public void setOpenVses(int openVses) {
        this.openVses = openVses;
    }

    public int getAllVses() {
        return allVses;
    }

    public void setAllVses(int allVses) {
        this.allVses = allVses;
    }

    public int getOpenEmes() {
        return openEmes;
    }

    public void setOpenEmes(int openEmes) {
        this.openEmes = openEmes;
    }

    public int getAllEmes() {
        return allEmes;
    }

    public void setAllEmes(int allEmes) {
        this.allEmes = allEmes;
    }

    public ArrayList<MessageBean> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageBean> messages) {
        this.messages = messages;
    }

    public DataQueryBean getAvgEqis() {
        return avgEqis;
    }

    public void setAvgEqis(DataQueryBean avgEqis) {
        this.avgEqis = avgEqis;
    }

    public int getUnHandleTask() {
        return unHandleTask;
    }

    public void setUnHandleTask(int unHandleTask) {
        this.unHandleTask = unHandleTask;
    }
}
