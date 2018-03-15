package com.isoftstone.smartsite.http.message;

/**
 * Created by gone on 2017/12/2.
 */

public class BeforeNMessageBean {
    private  int unreadPatrol;   //未读三方协同消息数
    private  int unreadMuckcar;  //未读渣土车监控消息数
    private  int unreadVideos;  //未读视频监控消息数
    private  int unreadTask;    //未读巡查任务消息数
    private  int unreadPlan;     //未读巡查计划消息数
    private  int unreadEnvironment;  //未读环境监测消息数

    private MessagePage patrol;  //三方协同消息
    private MessagePage environment;//环境监测消息
    private MessagePage task;        //巡查任务消息
    private MessagePage muckcar;  //渣土车监控消息
    private MessagePage video;  //视频监控消息
    private MessagePage plan;  //巡查计划消息

    public int getUnreadPatrol() {
        return unreadPatrol;
    }

    public void setUnreadPatrol(int unreadPatrol) {
        this.unreadPatrol = unreadPatrol;
    }

    public int getUnreadMuckcar() {
        return unreadMuckcar;
    }

    public void setUnreadMuckcar(int unreadMuckcar) {
        this.unreadMuckcar = unreadMuckcar;
    }

    public int getUnreadVideos() {
        return unreadVideos;
    }

    public void setUnreadVideos(int unreadVideos) {
        this.unreadVideos = unreadVideos;
    }

    public int getUnreadTask() {
        return unreadTask;
    }

    public void setUnreadTask(int unreadTask) {
        this.unreadTask = unreadTask;
    }

    public int getUnreadPlan() {
        return unreadPlan;
    }

    public void setUnreadPlan(int unreadPlan) {
        this.unreadPlan = unreadPlan;
    }

    public int getUnreadEnvironment() {
        return unreadEnvironment;
    }

    public void setUnreadEnvironment(int unreadEnvironment) {
        this.unreadEnvironment = unreadEnvironment;
    }

    public MessagePage getPatrol() {
        return patrol;
    }

    public void setPatrol(MessagePage patrol) {
        this.patrol = patrol;
    }

    public MessagePage getEnvironment() {
        return environment;
    }

    public void setEnvironment(MessagePage environment) {
        this.environment = environment;
    }

    public MessagePage getTask() {
        return task;
    }

    public void setTask(MessagePage task) {
        this.task = task;
    }

    public MessagePage getMuckcar() {
        return muckcar;
    }

    public void setMuckcar(MessagePage muckcar) {
        this.muckcar = muckcar;
    }

    public MessagePage getVideo() {
        return video;
    }

    public void setVideo(MessagePage video) {
        this.video = video;
    }

    public MessagePage getPlan() {
        return plan;
    }

    public void setPlan(MessagePage plan) {
        this.plan = plan;
    }
}
