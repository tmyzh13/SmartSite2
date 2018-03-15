package com.isoftstone.smartsite.http.user;

/**
 * Created by gone on 2017/10/14.
 */

public class LoginBean {
    private boolean isLoginSuccess = false;
    private String mErrorInfo ;
    private int mErrorCode = 0;   //1验证码超时   2验证码不正确  3用户不存在    4密码错误   5用户已锁定  6密码过期   7未知错误
    private String mName = "";    //用户登录姓名
    private String mPassword = "";  //用户登录密码
    private VideoParameter mVideoParameter;
    private String registerId = "";
    private UserBean mUserBean = null;      //用户详细信息

    public UserBean getmUserBean() {
        return mUserBean;
    }

    public void setmUserBean(UserBean mUserBean) {
        this.mUserBean = mUserBean;
    }

    public VideoParameter getmVideoParameter() {
        return mVideoParameter;
    }

    public void setmVideoParameter(VideoParameter mVideoParameter) {
        this.mVideoParameter = mVideoParameter;
    }

    public String getmErrorInfo() {
        return mErrorInfo;
    }

    public void setmErrorInfo(String mErrorInfo) {
        this.mErrorInfo = mErrorInfo;
    }

    public int getmErrorCode() {
        return mErrorCode;
    }

    public void setmErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
        switch (mErrorCode){
            case 1: mErrorInfo = "验证码超时";break;
            case 2: mErrorInfo = "验证码不正确";break;
            case 3: mErrorInfo = "用户名或密码错误";break;
            case 4: mErrorInfo = "用户名或密码错误";break;
            case 5: mErrorInfo = "用户已锁定";break;
            case 6: mErrorInfo = "密码过期";break;
            case 7: mErrorInfo = "未知错误";break;
        }
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public static  class VideoParameter{
        private String port;
        private String ip;
        private String loginName;
        private String loginPass;

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getLoginPass() {
            return loginPass;
        }

        public void setLoginPass(String loginPass) {
            this.loginPass = loginPass;
        }

        @Override
        public String toString() {
            return "VideoParameter{" +
                    "port='" + port + '\'' +
                    ", ip='" + ip + '\'' +
                    ", loginName='" + loginName + '\'' +
                    ", loginPass='" + loginPass + '\'' +
                    '}';
        }
    }
}
