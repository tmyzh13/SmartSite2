package com.isoftstone.smartsite.http.pageable;

/**
 * Created by gone on 2017/11/18.
 */

/*
分页参数
 */
public class PageableBean {
    public  String  page = "0";		//第几页
    public  String  size = "10";	//每页最大显示数
    public  String  sort ="";		//排序

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
