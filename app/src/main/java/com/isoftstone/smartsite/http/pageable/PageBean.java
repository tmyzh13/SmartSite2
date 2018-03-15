package com.isoftstone.smartsite.http.pageable;

/**
 * Created by gone on 2017/11/18.
 */
/*
分页响应
 */
public class PageBean {
    public boolean  first;	//是否第一页
    public boolean  last;	//	是否最后一页
    public int number;	//待补充
    public int numberOfElements;	//待补充
    public int size;	//	每页最大显示数
    public long totalElements;		//总数
    public int  totalPages;		//总页数

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
