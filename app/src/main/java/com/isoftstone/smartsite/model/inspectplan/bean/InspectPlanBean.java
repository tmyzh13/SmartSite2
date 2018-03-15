package com.isoftstone.smartsite.model.inspectplan.bean;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.util.Date;

/**
 * Created by zhang on 2017/11/18.
 */

public class InspectPlanBean {
    private long userId;//根据userId查询任务
    private String taskName;//任务名称
    private String address;//巡查点名称
    private String taskTimeStart;//开始时间
    private String taskTimeEnd;//结束时间
    private String taskCreateTime;//创建时间
    private int taskStatus;//巡查任务状态(1	已创建，待提交  2	已提交，待审批  3	已通过  4	已打回)
    private String users;//任务巡查人员
    private String patrolPositions;//任务巡查点位
    private String userName;//用户名称
    private String userCompany;//用户公司
    private BaseUserBean baseUserBean;

    public InspectPlanBean() {

    }

    public InspectPlanBean(int userId, String taskName, String address, String taskTimeStart, String taskTimeEnd, int taskStatus) {
        this.userId = userId;
        this.taskName = taskName;
        this.address = address;
        this.taskTimeStart = taskTimeStart;
        this.taskTimeEnd = taskTimeEnd;
        this.taskStatus = taskStatus;
    }

    public BaseUserBean getBaseUserBean() {
        return baseUserBean;
    }

    public void setBaseUserBean(BaseUserBean baseUserBean) {
        this.baseUserBean = baseUserBean;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    @Override
    public String toString() {
        return "InspectPlanBean{" +
                "userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", address='" + address + '\'' +
                ", taskTimeStart=" + taskTimeStart +
                ", taskTimeEnd=" + taskTimeEnd +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
