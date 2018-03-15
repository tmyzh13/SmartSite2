package com.isoftstone.smartsite.model.tripartite.data;

/**
 * Created by yanyongjun on 2017/10/31.
 */

public class ITime {
    public ITime(int year, int month, int day) {
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public int year;
    public int month;
    public int day;
}
