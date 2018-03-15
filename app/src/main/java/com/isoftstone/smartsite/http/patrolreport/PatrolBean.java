package com.isoftstone.smartsite.http.patrolreport;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/31.
 */

public class PatrolBean {
    private int id;	//	主键
    private BaseUserBean creator;//	创建人
    private  int status;//	状态
    private String date;//	date(yyyy-MM-dd HH:mm:ss)	创建时间
    private String  address; //巡查地点
    private String company; //巡查单位
    private String developmentCompany;//	建设单位
    private String constructionCompany;//	施工单位
    private String supervisionCompany;//		监理单位
    private boolean needVisit;  //	是否回访
    private String visitDate;  //	date(yyyy-MM-dd HH:mm:ss)	回访时间
    private String category;  //巡查类型
    private ArrayList<ReportBean> reports;

    public ArrayList<ReportBean> getReports() {
        return reports;
    }

    public void setReports(ArrayList<ReportBean> reports) {
        this.reports = reports;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseUserBean getCreator() {
        return creator;
    }

    public void setCreator(BaseUserBean creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDevelopmentCompany() {
        return developmentCompany;
    }

    public void setDevelopmentCompany(String developmentCompany) {
        this.developmentCompany = developmentCompany;
    }

    public String getConstructionCompany() {
        return constructionCompany;
    }

    public void setConstructionCompany(String constructionCompany) {
        this.constructionCompany = constructionCompany;
    }

    public String getSupervisionCompany() {
        return supervisionCompany;
    }

    public void setSupervisionCompany(String supervisionCompany) {
        this.supervisionCompany = supervisionCompany;
    }

    public boolean isVisit() {
        return needVisit;
    }

    public void setVisit(boolean visit) {
        needVisit = visit;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public boolean isNeedVisit() {
        return needVisit;
    }

    public void setNeedVisit(boolean needVisit) {
        this.needVisit = needVisit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
