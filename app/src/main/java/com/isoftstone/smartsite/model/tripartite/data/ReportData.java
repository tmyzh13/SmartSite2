package com.isoftstone.smartsite.model.tripartite.data;

import com.isoftstone.smartsite.http.patrolreport.PatrolBean;
import com.isoftstone.smartsite.model.message.data.MsgData;
import com.isoftstone.smartsite.utils.DateUtils;

import java.util.Date;

/**
 * Created by yanyongjun on 2017/10/16.
 */

public class ReportData extends PatrolBean {
    public final static int STATUS_DEFAULT = 1;
    public final static int STATUS_WAITTING_CHECK = 2;
    public final static int STATUS_WAITTING_REVISIT = 3;
    public final static int STATUS_REJECT = 4;
    public final static int STATUS_CHECKED = 5;
    private Date mDate = null;
    private Date mRevisitDate = null;

//    private int id;	//	主键
//    private String creator;//	创建人
//    private  int status;//	状态
//    private String date;//	date(yyyy-MM-dd HH:mm:ss)	创建时间
//    private String  address; //巡查地点
//    private String company; //巡查单位
//    private String developmentCompany;//	建设单位
//    private String constructionCompany;//	施工单位
//    private String supervisionCompany;//		监理单位
//    private boolean isVisit;  //	是否回访
//    private String visitDate;  //	date(yyyy-MM-dd HH:mm:ss)	回访时间

    public ReportData(PatrolBean data) {
        setId(data.getId());
        setCreator(data.getCreator());
        setStatus(data.getStatus());
        setDate(data.getDate());
        setAddress(data.getAddress());
        setCompany(data.getCompany());
        setDevelopmentCompany(data.getDevelopmentCompany());
        setConstructionCompany(data.getConstructionCompany());
        setSupervisionCompany(data.getSupervisionCompany());
        setVisit(data.isVisit());
        setVisitDate(data.getVisitDate());
    }

    public ReportData(){

    }

    public Date getFormatDate() {
        if (mDate != null) {
            return mDate;
        }
        try {
            mDate = MsgData.format5.parse(getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public Date getFormatRevisitDate(){
        if(mRevisitDate != null){
            return mRevisitDate;
        }
        try{
            mRevisitDate = DateUtils.format_yyyy_MM_dd_HH_mm_ss.parse(getVisitDate());
        }catch(Exception e){
            e.printStackTrace();
        }
        return mRevisitDate;
    }

    /**
     * 判断这个是否是巡查者
     * @param accountName
     * @return
     */
    public boolean isExaminer(String accountName){
        //TODO
        return true;
    }

    @Override
    public String toString() {
        return getId() + ":" + getCreator() + ":" + getStatus() + ":" + getDate() + ":" + getAddress() + ":" +
                getCompany() + ":" + getDevelopmentCompany() + ":" + getConstructionCompany() + ":" +
                getSupervisionCompany() + ":" + isVisit() + ":" + getVisitDate();
    }
}
