package com.isoftstone.smartsite.http.patroltask;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gone on 2017/11/16.
 */

/*
巡查任务
 */
public class PatrolTaskBean implements Serializable{

    private static final long serialVersionUID = 0x0004L;

    private long taskId;	//任务ID
    private String taskName;//任务名称
    private ArrayList<BaseUserBean>  users; //巡查人员
    private ArrayList<PatrolPositionBean>	patrolPositions;//巡查地点
    private String taskTimeStart;//巡查任务规定开始时间
    private String taskTimeEnd; //	巡查任务规定结束时间
    private String taskStart;	//巡查任务实际开始时间
    private String taskEnd; 	//巡查任务实际结束时间
    private  int taskStatus;    //巡查任务状态    0：未巡查，1：正在巡查，2：完成巡查
    private int taskType;		//巡查任务创建类型
    private BaseUserBean  creator;   //任务创建人
    private int planStatus;  	//计划状态
    private String taskContent;	 //任务内容
    private String createTime;	//任务创建时间


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ArrayList<BaseUserBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<BaseUserBean> users) {
        this.users = users;
    }

    public ArrayList<PatrolPositionBean> getPatrolPositions() {
        return patrolPositions;
    }

    public void setPatrolPositions(ArrayList<PatrolPositionBean> patrolPositions) {
        this.patrolPositions = patrolPositions;
    }

    public String getTaskTimeStart() {
        return taskTimeStart;
    }

    public void setTaskTimeStart(String taskTimeStart) {
        this.taskTimeStart = taskTimeStart;
    }

    public String getTaskTimeEnd() {
        return taskTimeEnd;
    }

    public void setTaskTimeEnd(String taskTimeEnd) {
        this.taskTimeEnd = taskTimeEnd;
    }

    public String getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(String taskStart) {
        this.taskStart = taskStart;
    }

    public String getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(String taskEnd) {
        this.taskEnd = taskEnd;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public BaseUserBean getCreator() {
        return creator;
    }

    public void setCreator(BaseUserBean creator) {
        this.creator = creator;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PatrolTaskBean{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", users=" + users +
                ", patrolPositions=" + patrolPositions +
                ", taskTimeStart='" + taskTimeStart + '\'' +
                ", taskTimeEnd='" + taskTimeEnd + '\'' +
                ", taskStart='" + taskStart + '\'' +
                ", taskEnd='" + taskEnd + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskType=" + taskType +
                ", creator=" + creator +
                ", planStatus=" + planStatus +
                ", taskContent='" + taskContent + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
