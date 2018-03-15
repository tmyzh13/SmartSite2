package com.isoftstone.smartsite.http.patrolinfo;

import com.isoftstone.smartsite.http.user.BaseUserBean;

/**
 * Created by gone on 2017/11/22.
 */

public class UserTaskCountBean {
    private BaseUserBean user;//	用户实体
    private int offCount;	//Integer	当前用户已完成的任务数量
    private int unCount;	//Integer	当前用户未完成的任务数量

    public BaseUserBean getUser() {
        return user;
    }

    public void setUser(BaseUserBean user) {
        this.user = user;
    }

    public int getOffCount() {
        return offCount;
    }

    public void setOffCount(int offCount) {
        this.offCount = offCount;
    }

    public int getUnCount() {
        return unCount;
    }

    public void setUnCount(int unCount) {
        this.unCount = unCount;
    }

    @Override
    public String toString() {
        return "UserTaskCountBean{" +
                "user=" + user +
                ", offCount=" + offCount +
                ", unCount=" + unCount +
                '}';
    }
}
