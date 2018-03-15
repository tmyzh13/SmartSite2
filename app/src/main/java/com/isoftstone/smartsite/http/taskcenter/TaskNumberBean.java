package com.isoftstone.smartsite.http.taskcenter;

import android.util.Log;

/**
 * Created by gone on 2017/11/18.
 */

public class TaskNumberBean {
    private long pendingApprovalPlan;	//待处理的审批计划
    private long pendingCarriedtasks;	//待执行的任务
    private long pedingReport;  		//待处理的报告
    private long pendingIdentifiedLicense;	//待识别的车辆数目


    public long getPendingApprovalPlan() {
        return pendingApprovalPlan;
    }

    public void setPendingApprovalPlan(long pendingApprovalPlan) {
        this.pendingApprovalPlan = pendingApprovalPlan;
    }

    public long getPendingCarriedtasks() {
        return pendingCarriedtasks;
    }

    public void setPendingCarriedtasks(long pendingCarriedtasks) {
        this.pendingCarriedtasks = pendingCarriedtasks;
    }

    public long getPedingReport() {
        return pedingReport;
    }

    public void setPedingReport(long pedingReport) {
        this.pedingReport = pedingReport;
    }

    public long getPendingIdentifiedLicense() {
        return pendingIdentifiedLicense;
    }

    public void setPendingIdentifiedLicense(long pendingIdentifiedLicense) {
        this.pendingIdentifiedLicense = pendingIdentifiedLicense;
    }
}
