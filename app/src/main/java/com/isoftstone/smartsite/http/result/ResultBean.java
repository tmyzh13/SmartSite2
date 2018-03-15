package com.isoftstone.smartsite.http.result;

/**
 * Created by gone on 2017/12/4.
 */

public class ResultBean {
    private  boolean success;	    //boolean	是否成功，总是false
    private  String  code;	        //string	异常码
    private  String  msg;	        //string	异常信息
    private  String  description;	//string	说明
    private  String  solution;	    //string	解决方案

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
