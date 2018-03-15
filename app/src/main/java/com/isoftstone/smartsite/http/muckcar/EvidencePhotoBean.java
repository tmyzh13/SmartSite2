package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.user.BaseUserBean;

/**
 * Created by gone on 2017/11/16.
 */
/*
取证照片
 */
public class EvidencePhotoBean {
    private String licence;  //车牌号
    private String takePhotoTime; //拍照时间
    private String uploadPeople;  //上传人
    private String photoSrc;    //图片路径
    private String smallPhotoSrc; //
    private String addr;
    private String speed;
    private BaseUserBean takePhoroUser;

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getTakePhotoTime() {
        return takePhotoTime;
    }

    public void setTakePhotoTime(String takePhotoTime) {
        this.takePhotoTime = takePhotoTime;
    }

    public String getUploadPeople() {
        return uploadPeople;
    }

    public void setUploadPeople(String uploadPeople) {
        this.uploadPeople = uploadPeople;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public BaseUserBean getTakePhoroUser() {
        return takePhoroUser;
    }

    public void setTakePhoroUser(BaseUserBean takePhoroUser) {
        this.takePhoroUser = takePhoroUser;
    }

    public String getSmallPhotoSrc() {
        return smallPhotoSrc;
    }

    public void setSmallPhotoSrc(String smallPhotoSrc) {
        this.smallPhotoSrc = smallPhotoSrc;
    }

    @Override
    public String toString() {
        return "EvidencePhotoBean{" +
                "licence='" + licence + '\'' +
                ", takePhotoTime='" + takePhotoTime + '\'' +
                ", uploadPeople='" + uploadPeople + '\'' +
                ", photoSrc='" + photoSrc + '\'' +
                ", smallPhotoSrc='" + smallPhotoSrc + '\'' +
                ", addr='" + addr + '\'' +
                ", speed='" + speed + '\'' +
                ", takePhoroUser=" + takePhoroUser +
                '}';
    }
}
