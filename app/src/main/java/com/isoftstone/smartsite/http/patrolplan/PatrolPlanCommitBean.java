package com.isoftstone.smartsite.http.patrolplan;

import com.isoftstone.smartsite.http.user.SimpleUserBean;

/**
 * Created by gone on 2017/11/18.
 */

public class PatrolPlanCommitBean {
    private SimpleUserBean creator;
    private String taskTimeStart;
    private String taskTimeEnd;

    public SimpleUserBean getCreator() {
        return creator;
    }

    public void setCreator(SimpleUserBean creator) {
        this.creator = creator;
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
}
