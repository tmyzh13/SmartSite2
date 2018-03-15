package com.isoftstone.smartsite.http.patroluser;

import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.io.Serializable;

/**
 * Created by gone on 2017/11/16.
 */

/*
人员轨迹实体
 */
public class UserTrackBean implements Serializable{

    private static final long serialVersionUID = 0x0003L;

    private long id;//		主键
    private int userId;  //用户id
    private double longitude;  //人员轨迹经度
    private double latitude;	//人员轨迹纬度
    private long taskId;     //巡查任务Id
    private String updateTime;  //接收轨迹的时间
    private BaseUserBean user;  	//用户返回数据中封装用户信息
    private PatrolTaskBean patrolTask;		//用于返回巡查当前用户的任务

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public BaseUserBean getUser() {
        return user;
    }

    public void setUser(BaseUserBean user) {
        this.user = user;
    }

    public PatrolTaskBean getPatrolTask() {
        return patrolTask;
    }

    public void setPatrolTask(PatrolTaskBean patrolTask) {
        this.patrolTask = patrolTask;
    }

    @Override
    public String toString() {
        return "UserTrackBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", taskId=" + taskId +
                ", updateTime='" + updateTime + '\'' +
                ", user=" + user +
                ", patrolTask=" + patrolTask +
                '}';
    }
}
