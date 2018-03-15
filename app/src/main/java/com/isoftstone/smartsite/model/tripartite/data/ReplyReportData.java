package com.isoftstone.smartsite.model.tripartite.data;

import com.isoftstone.smartsite.http.patrolreport.PatrolBean;

/**
 * Created by yanyongjun on 2017/10/29.
 */

public class ReplyReportData {
    private PatrolBean mPatrolBean = null;

    public void setPatrolBean(PatrolBean data) {
        mPatrolBean = data;
    }

    public PatrolBean getPatrolBean() {
        return mPatrolBean;
    }

}
