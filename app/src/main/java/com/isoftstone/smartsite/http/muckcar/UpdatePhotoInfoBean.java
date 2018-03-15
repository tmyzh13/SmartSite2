package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.user.SimpleUserBean;

/**
 * Created by gone on 2017/11/18.
 */

public class UpdatePhotoInfoBean {
   private SimpleUserBean takePhoroUser;
   private  String licence;
   private  String  addr;
   private  String photoSrc;
   private  String takePhotoTime;

    public SimpleUserBean getTakePhoroUser() {
        return takePhoroUser;
    }

    public void setTakePhoroUser(SimpleUserBean takePhoroUser) {
        this.takePhoroUser = takePhoroUser;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
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

    public String getTakePhotoTime() {
        return takePhotoTime;
    }

    public void setTakePhotoTime(String takePhotoTime) {
        this.takePhotoTime = takePhotoTime;
    }

    @Override
    public String toString(){
        return "takePhoroUser:"+takePhoroUser+" licence:"+licence+" addr:"+addr +" photoSrc:"+photoSrc +" takePhotoTime:"+takePhotoTime;
    }
}
