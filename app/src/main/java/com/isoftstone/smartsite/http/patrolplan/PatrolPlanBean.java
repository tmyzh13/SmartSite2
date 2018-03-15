package com.isoftstone.smartsite.http.patrolplan;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.io.Serializable;

/**
 * Created by gone on 2017/11/16.
 */
/*
巡查计划
 */
public class PatrolPlanBean  implements Serializable {
    private static final long serialVersionUID = 0x0011L;
    private long id;          //主键
    private  int status;      //状态  1	已创建，待提交  2	已提交，待审批  3	已通过  4	已打回
    private String title;     //标题
    private BaseUserBean creator;//用户
    private int weekOfYear;  //一年的第几周
    private String startDate;    //开始时间
    private String endDate;  //结束时间
    private String date;     //创建时间


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BaseUserBean getCreator() {
        return creator;
    }

    public void setCreator(BaseUserBean creator) {
        this.creator = creator;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public String getStart() {
        return startDate;
    }

    public void setStart(String start) {
        this.startDate = start;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PatrolPlanBean{" +
                "id=" + id +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", creator=" + creator +
                ", weekOfYear=" + weekOfYear +
                ", start='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
