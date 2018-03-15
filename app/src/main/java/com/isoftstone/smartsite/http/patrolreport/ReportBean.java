package com.isoftstone.smartsite.http.patrolreport;

import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/10/31.
 */

public class ReportBean {
    private int id;           //	主键
    private int category; //1:回访,2:回复,3:验收
    private BaseUserBean creator;//创建人，即报告人
    private String date;	//date(yyyy-MM-dd HH:mm:ss)	创建时间
    private String content;//	string	内容
    private int status;//	状态，参考Patrol状态说明
    private String name;//	巡查名称
    private String reportImages;//	报告图片
    private String files;	//	报告附件
    private String patrolUser; //	巡查人
    private String patrolDateStart;//	date(yyyy-MM-dd HH:mm)	巡查起始时间
    private String patrolDateEnd;//	date(yyyy-MM-dd HH:mm)	巡查结束时间
    private String patrolContent;	//string	巡查内容
    private boolean needVisit;;
    private String visitDate;
    private ArrayList<String> reportFiles;
    private ArrayList<String> smallImagesList;
    private String smallFiles;



    private PatrolBean patrol;

    public PatrolBean getPatrol() {
        return patrol;
    }
    public ArrayList<String> getReportFiles(){
        return reportFiles;
    }
    public void setReportFiles(ArrayList<String> reportFiles){
        this.reportFiles = reportFiles;
    }

    public void setPatrol(PatrolBean patrol) {
        this.patrol = patrol;
    }

    public void setSmallImagesList(ArrayList<String> temp){
        this.smallImagesList = temp;
    }

    public ArrayList<String> getSmallImagesList(){
        return smallImagesList;
    }

    public void setSmallImages(String smallImages){
        this.smallFiles = smallImages;
    }

    public String getSmallImages(){
        return this.smallFiles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public BaseUserBean getCreator() {
        return creator;
    }

    public void setCreator(BaseUserBean creator) {
        this.creator = creator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportImages() {
        return reportImages;
    }

    public void setReportImages(String reportImages) {
        this.reportImages = reportImages;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String reportFiles) {
        this.files = reportFiles;
    }

    public String getPatrolUser() {
        return patrolUser;
    }

    public void setPatrolUser(String patrolUser) {
        this.patrolUser = patrolUser;
    }

    public String getPatrolDateStart() {
        return patrolDateStart;
    }

    public void setPatrolDateStart(String patrolDateStart) {
        this.patrolDateStart = patrolDateStart;
    }

    public String getPatrolDateEnd() {
        return patrolDateEnd;
    }

    public void setPatrolDateEnd(String patrolDateEnd) {
        this.patrolDateEnd = patrolDateEnd;
    }

    public String getPatrolContent() {
        return patrolContent;
    }

    public void setPatrolContent(String patrolContent) {
        this.patrolContent = patrolContent;
    }

    public boolean isVisit() {
        return needVisit;
    }

    public void setVisit(boolean visit) {
        this.needVisit = visit;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}
