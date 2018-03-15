package com.isoftstone.smartsite.model.dirtcar.bean;

import java.util.Date;

/**
 * Created by zhang on 2017/11/18.
 */

public class ManualPhotographyBean {
    private String licence;//车牌号
    private String takePhotoUserHeadPath;//拍照上传用户头像路径
    private String takePhotoUserName;//拍照上传用户名称
    private String takePhotoUserCompany;//拍照上传用户头像路径
    private String takePhotoTime;//拍照时间（格式:“yyyy-MM-dd HH:mm:ss”）
    private String addr;//拍照地点
    private String photoSrc;//拍照上图片（每张图片之间用逗号隔开）

    public ManualPhotographyBean() {

    }

    public ManualPhotographyBean(String licence, String takePhotoUserHeadPath, String takePhotoUserName, String takePhotoTime, String addr, String photoSrc, String takePhotoUserCompany) {
        this.licence = licence;
        this.takePhotoUserHeadPath = takePhotoUserHeadPath;
        this.takePhotoUserName = takePhotoUserName;
        this.takePhotoTime = takePhotoTime;
        this.addr = addr;
        this.photoSrc = photoSrc;
        this.takePhotoUserCompany = takePhotoUserCompany;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getTakePhotoUserHeadPath() {
        return takePhotoUserHeadPath;
    }

    public void setTakePhotoUserHeadPath(String takePhotoUserHeadPath) {
        this.takePhotoUserHeadPath = takePhotoUserHeadPath;
    }

    public String getTakePhotoUserName() {
        return takePhotoUserName;
    }

    public void setTakePhotoUserName(String takePhotoUserName) {
        this.takePhotoUserName = takePhotoUserName;
    }

    public String getTakePhotoTime() {
        return takePhotoTime;
    }

    public void setTakePhotoTime(String takePhotoTime) {
        this.takePhotoTime = takePhotoTime;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public String getTakePhotoUserCompany() {
        return takePhotoUserCompany;
    }

    public void setTakePhotoUserCompany(String takePhotoUserCompany) {
        this.takePhotoUserCompany = takePhotoUserCompany;
    }

    @Override
    public String toString() {
        return "ManualPhotographyBean{" +
                "licence='" + licence + '\'' +
                ", takePhotoUserHeadPath='" + takePhotoUserHeadPath + '\'' +
                ", takePhotoUserName='" + takePhotoUserName + '\'' +
                ", takePhotoTime='" + takePhotoTime + '\'' +
                ", addr='" + addr + '\'' +
                ", photoSrc='" + photoSrc + '\'' +
                '}';
    }
}
