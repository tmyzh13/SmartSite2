package com.isoftstone.smartsite.model.inspectplan.data;

import android.net.Uri;
import android.view.View;

import com.isoftstone.smartsite.http.user.BaseUserBean;

/**
 * Created by Administrator on 2017-11-24.
 */

public class InspectorData {
//    private   String  sort="";                //名字对应的字母
//    private  String  contactName = "";      //联系人姓名
//    private Uri iconUri = null;             //头像地址
//    private  boolean selected=false;     //是否被选中
//    private  int visible= View.VISIBLE;      //是否显示字母    View.GONE  隐藏      View.INVISIBLE  不可见，但仍然占据空间     View.VISIBLE   可见
//
//    public String getSort(){
//        return  sort;
//    }
//
//    public void setSort(String sort){
//        this.sort = sort;
//    }
//
//    public String getContactName(){
//        return  contactName;
//    }
//
//    public void setContactName(String contactName){
//        this.contactName = contactName;
//    }
//
//    public Uri getIconUri(){
//        return iconUri;
//    }
//
//    public void setIconUri(Uri iconUri){
//        this.iconUri = iconUri;
//    }
//
//    public boolean getSelected(){
//        return selected;
//    }
//
//    public void setSelected(boolean selected){
//        this.selected = selected;
//    }
//
//    public int getVisible(){
//        return visible;
//    }
//
//    public void setVisible(int visible){
//        this.visible = visible;
//    }
public Long id;       //	用户ID
    public String account = ""; //用户账号
    public String name = "";     //用户名称
    public String password = "";   //	密码
    public String imageData = "";   //用户头像(保存路径)
    public String telephone = "";   //	String	电话
    public String fax = "";         //	传真
    public String email = "";     //	邮件
    public String address = "";       //	地址
    public String description = "";   //	描述
    public String departmentId = "";  //	机构id
    public String employeeCode = "";  //	员工编号
    public String createTime = "";   //	Date(yyyy-MM-dd HH:mm:ss)	创建时间
    public Long creator;        //创建人
    public int accountType = 0; //	 账号类型
    public int resetPwd  = 0;//	 重置密码
    public int locked = 0;//	是否锁定（0未锁定，1锁定）
    public int delFlag = 0;//	删除标记
    public int sex = 0	;//	    性别（0：男，1：女）
    public String registerId = "";//	极光推送Id
    public boolean isSelected = false;
    public String sort = "";
    public int isVisible = View.VISIBLE;
    //public String roles;	//Set<Role>	关联的角色
    //public String archs;	//Set<Arch>	关联的区域



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(int resetPwd) {
        this.resetPwd = resetPwd;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public void setIsSelected(boolean isSelectd){
        this.isSelected = isSelectd;
    }

    public String getSort(){
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getIsVisible(){
        return isVisible;
    }

    public void setIsVisible(int isVisible){
        this.isVisible = isVisible;
    }
}
