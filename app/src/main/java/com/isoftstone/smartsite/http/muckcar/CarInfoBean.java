package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.BaseBean.ArchBean;

/**
 * Created by gone on 2017/11/16.
 */

public class CarInfoBean {
    private int isAlarmMc;  //当前区域黑名单渣土车流量
    private int noAlarmMc;  //当前区域白名单渣土车流量
    private ArchBean arch;  //区域实体
    private int count ;   //当前区域总流量

    public int getIsAlarmMc() {
        return isAlarmMc;
    }

    public void setIsAlarmMc(int isAlarmMc) {
        this.isAlarmMc = isAlarmMc;
    }

    public int getNoAlarmMc() {
        return noAlarmMc;
    }

    public void setNoAlarmMc(int noAlarmMc) {
        this.noAlarmMc = noAlarmMc;
    }

    public ArchBean getArch() {
        return arch;
    }

    public void setArch(ArchBean arch) {
        this.arch = arch;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
