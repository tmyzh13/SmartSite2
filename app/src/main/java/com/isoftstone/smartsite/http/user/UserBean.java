package com.isoftstone.smartsite.http.user;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import java.io.Serializable;

/**
 * Created by gone on 2017/11/1.
 */

public class UserBean implements Serializable{

    private static final long serialVersionUID = 0x0005L;

    private  BaseUserBean loginUser;  //用户信息；
    private Permission privilegeCode = null;//用户权限信息

    public Permission getmPermission() {
        return privilegeCode;
    }

    public void setmPermission(Permission mPermission) {
        this.privilegeCode = mPermission;
    }

    public BaseUserBean getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(BaseUserBean loginUser) {
        this.loginUser = loginUser;
    }

    public  static class Permission implements Serializable{

        private static final long serialVersionUID = 0x0007L;

        private boolean B_USER_ADD = false;
        private boolean B_PATROL_ACCEPT = false;
        private boolean B_ARCH_ADD = false;
        private boolean ENVIRMENT_VIEW_DATAQUERY = false;  //数据查询
        private boolean MENU_2 = false;
        private boolean VS_MAP = false;
        private boolean B_ARCH_DELETE = false;
        private boolean MENU_1 = false;
        private boolean B_USER_UPDATE = false;
        private boolean B_USER_RESETPWD = false;
        private boolean ENVIRMENT_VIEW_MAP = false;   //站点地图
        private boolean B_PATROL_REPLY = false;
        private boolean B_USER_UNLOCK = false;
        private boolean B_USER_DELETE = false;
        private boolean B_DEVICE_ADD = false;
        private boolean B_ROLE_UPDATE = false;
        private boolean M_SYSTEMCONFIG = false;
        private boolean MANUAL_RECONGIZED = false;
        private boolean M_PATROL = false;
        private boolean B_ARCH_UPDATE = false;
        private boolean M_LOG = false;
        private boolean VS_RECORD = false;
        private boolean B_ROLE_DELETE = false;
        private boolean M_PLAN = false;
        private boolean M_DEVICE = false;
        private boolean B_DEVICE_DELETE = false;
        private boolean B_DEVICE_THRESHOLDSET = false;
        private boolean B_DEVICE_TONGBU = false;
        private boolean MESSAGE_CENTER = false;
        private boolean B_PATROL_ADD = false;
        private boolean B_DEVICE_UPDATE = false;
        private boolean M_PATROL_REPORT = false;    //巡查报告权限
        private boolean M_PATROL_ACCEPT = false;    //验收报告权限
        private boolean MUCKCAR_TRACK = false;      //渣土车追踪
        private boolean M_MESSAGE_DELETE_ROLE = false;
        private boolean B_PATROL_VISIT = false;
        private boolean MUCKCAR_MONITOR = false;      //渣土车监控
        private boolean M_ROLE = false;
        private boolean M_MESSAGE = false;
        private boolean ENVIRMENT_VIEW = false;
        private boolean M_ARCH = false;
        private boolean M_MESSAGE_ADD_ROLE = false;
        private boolean M_USER = false;
        private boolean B_ROLE_ADD = false;
        private boolean VS = false;
        private boolean M_CPPA = false;   //巡查计划审批
        private boolean M_CPP = false;    //巡查计划
        private boolean M_CPT = false;    //巡查任务
        private boolean M_CPINFO = false; //巡查概况
        private boolean M_CPM = false;   //巡查监控

        public boolean isM_CPM() {
            return M_CPM;
        }

        public void setM_CPM(boolean m_CPM) {
            M_CPM = m_CPM;
        }

        public boolean isM_CPINFO() {
            return M_CPINFO;
        }

        public void setM_CPINFO(boolean m_CPINFO) {
            M_CPINFO = m_CPINFO;
        }

        public boolean isM_CPT() {
            return M_CPT;
        }

        public void setM_CPT(boolean m_CPT) {
            M_CPT = m_CPT;
        }

        public boolean isB_USER_ADD() {
            return B_USER_ADD;
        }

        public void setB_USER_ADD(boolean b_USER_ADD) {
            B_USER_ADD = b_USER_ADD;
        }

        public boolean isB_PATROL_ACCEPT() {
            return B_PATROL_ACCEPT;
        }

        public void setB_PATROL_ACCEPT(boolean b_PATROL_ACCEPT) {
            B_PATROL_ACCEPT = b_PATROL_ACCEPT;
        }

        public boolean isB_ARCH_ADD() {
            return B_ARCH_ADD;
        }

        public void setB_ARCH_ADD(boolean b_ARCH_ADD) {
            B_ARCH_ADD = b_ARCH_ADD;
        }

        public boolean isENVIRMENT_VIEW_DATAQUERY() {
            return ENVIRMENT_VIEW_DATAQUERY;
        }

        public void setENVIRMENT_VIEW_DATAQUERY(boolean ENVIRMENT_VIEW_DATAQUERY) {
            this.ENVIRMENT_VIEW_DATAQUERY = ENVIRMENT_VIEW_DATAQUERY;
        }

        public boolean isMENU_2() {
            return MENU_2;
        }

        public void setMENU_2(boolean MENU_2) {
            this.MENU_2 = MENU_2;
        }

        public boolean isVS_MAP() {
            return VS_MAP;
        }

        public void setVS_MAP(boolean VS_MAP) {
            this.VS_MAP = VS_MAP;
        }

        public boolean isB_ARCH_DELETE() {
            return B_ARCH_DELETE;
        }

        public void setB_ARCH_DELETE(boolean b_ARCH_DELETE) {
            B_ARCH_DELETE = b_ARCH_DELETE;
        }

        public boolean isMENU_1() {
            return MENU_1;
        }

        public void setMENU_1(boolean MENU_1) {
            this.MENU_1 = MENU_1;
        }

        public boolean isB_USER_UPDATE() {
            return B_USER_UPDATE;
        }

        public void setB_USER_UPDATE(boolean b_USER_UPDATE) {
            B_USER_UPDATE = b_USER_UPDATE;
        }

        public boolean isB_USER_RESETPWD() {
            return B_USER_RESETPWD;
        }

        public void setB_USER_RESETPWD(boolean b_USER_RESETPWD) {
            B_USER_RESETPWD = b_USER_RESETPWD;
        }

        public boolean isENVIRMENT_VIEW_MAP() {
            return ENVIRMENT_VIEW_MAP;
        }

        public void setENVIRMENT_VIEW_MAP(boolean ENVIRMENT_VIEW_MAP) {
            this.ENVIRMENT_VIEW_MAP = ENVIRMENT_VIEW_MAP;
        }

        public boolean isB_PATROL_REPLY() {
            return B_PATROL_REPLY;
        }

        public void setB_PATROL_REPLY(boolean b_PATROL_REPLY) {
            B_PATROL_REPLY = b_PATROL_REPLY;
        }

        public boolean isB_USER_UNLOCK() {
            return B_USER_UNLOCK;
        }

        public void setB_USER_UNLOCK(boolean b_USER_UNLOCK) {
            B_USER_UNLOCK = b_USER_UNLOCK;
        }

        public boolean isB_USER_DELETE() {
            return B_USER_DELETE;
        }

        public void setB_USER_DELETE(boolean b_USER_DELETE) {
            B_USER_DELETE = b_USER_DELETE;
        }

        public boolean isB_DEVICE_ADD() {
            return B_DEVICE_ADD;
        }

        public void setB_DEVICE_ADD(boolean b_DEVICE_ADD) {
            B_DEVICE_ADD = b_DEVICE_ADD;
        }

        public boolean isB_ROLE_UPDATE() {
            return B_ROLE_UPDATE;
        }

        public void setB_ROLE_UPDATE(boolean b_ROLE_UPDATE) {
            B_ROLE_UPDATE = b_ROLE_UPDATE;
        }

        public boolean isM_SYSTEMCONFIG() {
            return M_SYSTEMCONFIG;
        }

        public void setM_SYSTEMCONFIG(boolean m_SYSTEMCONFIG) {
            M_SYSTEMCONFIG = m_SYSTEMCONFIG;
        }

        public boolean isMANUAL_RECONGIZED() {
            return MANUAL_RECONGIZED;
        }

        public void setMANUAL_RECONGIZED(boolean MANUAL_RECONGIZED) {
            this.MANUAL_RECONGIZED = MANUAL_RECONGIZED;
        }

        public boolean isM_PATROL() {
            return M_PATROL;
        }

        public void setM_PATROL(boolean m_PATROL) {
            M_PATROL = m_PATROL;
        }

        public boolean isB_ARCH_UPDATE() {
            return B_ARCH_UPDATE;
        }

        public void setB_ARCH_UPDATE(boolean b_ARCH_UPDATE) {
            B_ARCH_UPDATE = b_ARCH_UPDATE;
        }

        public boolean isM_LOG() {
            return M_LOG;
        }

        public void setM_LOG(boolean m_LOG) {
            M_LOG = m_LOG;
        }

        public boolean isVS_RECORD() {
            return VS_RECORD;
        }

        public void setVS_RECORD(boolean VS_RECORD) {
            this.VS_RECORD = VS_RECORD;
        }

        public boolean isB_ROLE_DELETE() {
            return B_ROLE_DELETE;
        }

        public void setB_ROLE_DELETE(boolean b_ROLE_DELETE) {
            B_ROLE_DELETE = b_ROLE_DELETE;
        }

        public boolean isM_PLAN() {
            return M_PLAN;
        }

        public void setM_PLAN(boolean m_PLAN) {
            M_PLAN = m_PLAN;
        }

        public boolean isM_DEVICE() {
            return M_DEVICE;
        }

        public void setM_DEVICE(boolean m_DEVICE) {
            M_DEVICE = m_DEVICE;
        }

        public boolean isB_DEVICE_DELETE() {
            return B_DEVICE_DELETE;
        }

        public void setB_DEVICE_DELETE(boolean b_DEVICE_DELETE) {
            B_DEVICE_DELETE = b_DEVICE_DELETE;
        }

        public boolean isB_DEVICE_THRESHOLDSET() {
            return B_DEVICE_THRESHOLDSET;
        }

        public void setB_DEVICE_THRESHOLDSET(boolean b_DEVICE_THRESHOLDSET) {
            B_DEVICE_THRESHOLDSET = b_DEVICE_THRESHOLDSET;
        }

        public boolean isB_DEVICE_TONGBU() {
            return B_DEVICE_TONGBU;
        }

        public void setB_DEVICE_TONGBU(boolean b_DEVICE_TONGBU) {
            B_DEVICE_TONGBU = b_DEVICE_TONGBU;
        }

        public boolean isMESSAGE_CENTER() {
            return MESSAGE_CENTER;
        }

        public void setMESSAGE_CENTER(boolean MESSAGE_CENTER) {
            this.MESSAGE_CENTER = MESSAGE_CENTER;
        }

        public boolean isB_PATROL_ADD() {
            return B_PATROL_ADD;
        }

        public void setB_PATROL_ADD(boolean b_PATROL_ADD) {
            B_PATROL_ADD = b_PATROL_ADD;
        }

        public boolean isB_DEVICE_UPDATE() {
            return B_DEVICE_UPDATE;
        }

        public void setB_DEVICE_UPDATE(boolean b_DEVICE_UPDATE) {
            B_DEVICE_UPDATE = b_DEVICE_UPDATE;
        }

        public boolean isM_PATROL_REPORT() {
            return M_PATROL_REPORT;
        }

        public void setM_PATROL_REPORT(boolean m_PATROL_REPORT) {
            M_PATROL_REPORT = m_PATROL_REPORT;
        }

        public boolean isM_PATROL_ACCEPT() {
            return M_PATROL_ACCEPT;
        }

        public void setM_PATROL_ACCEPT(boolean m_PATROL_ACCEPT) {
            M_PATROL_ACCEPT = m_PATROL_ACCEPT;
        }

        public boolean isMUCKCAR_TRACK() {
            return MUCKCAR_TRACK;
        }

        public void setMUCKCAR_TRACK(boolean MUCKCAR_TRACK) {
            this.MUCKCAR_TRACK = MUCKCAR_TRACK;
        }

        public boolean isM_MESSAGE_DELETE_ROLE() {
            return M_MESSAGE_DELETE_ROLE;
        }

        public void setM_MESSAGE_DELETE_ROLE(boolean m_MESSAGE_DELETE_ROLE) {
            M_MESSAGE_DELETE_ROLE = m_MESSAGE_DELETE_ROLE;
        }

        public boolean isB_PATROL_VISIT() {
            return B_PATROL_VISIT;
        }

        public void setB_PATROL_VISIT(boolean b_PATROL_VISIT) {
            B_PATROL_VISIT = b_PATROL_VISIT;
        }

        public boolean isMUCKCAR_MONITOR() {
            return MUCKCAR_MONITOR;
        }

        public void setMUCKCAR_MONITOR(boolean MUCKCAR_MONITOR) {
            this.MUCKCAR_MONITOR = MUCKCAR_MONITOR;
        }

        public boolean isM_ROLE() {
            return M_ROLE;
        }

        public void setM_ROLE(boolean m_ROLE) {
            M_ROLE = m_ROLE;
        }

        public boolean isM_MESSAGE() {
            return M_MESSAGE;
        }

        public void setM_MESSAGE(boolean m_MESSAGE) {
            M_MESSAGE = m_MESSAGE;
        }

        public boolean isENVIRMENT_VIEW() {
            return ENVIRMENT_VIEW;
        }

        public void setENVIRMENT_VIEW(boolean ENVIRMENT_VIEW) {
            this.ENVIRMENT_VIEW = ENVIRMENT_VIEW;
        }

        public boolean isM_ARCH() {
            return M_ARCH;
        }

        public void setM_ARCH(boolean m_ARCH) {
            M_ARCH = m_ARCH;
        }

        public boolean isM_MESSAGE_ADD_ROLE() {
            return M_MESSAGE_ADD_ROLE;
        }

        public void setM_MESSAGE_ADD_ROLE(boolean m_MESSAGE_ADD_ROLE) {
            M_MESSAGE_ADD_ROLE = m_MESSAGE_ADD_ROLE;
        }

        public boolean isM_USER() {
            return M_USER;
        }

        public void setM_USER(boolean m_USER) {
            M_USER = m_USER;
        }

        public boolean isB_ROLE_ADD() {
            return B_ROLE_ADD;
        }

        public void setB_ROLE_ADD(boolean b_ROLE_ADD) {
            B_ROLE_ADD = b_ROLE_ADD;
        }

        public boolean isVS() {
            return VS;
        }

        public void setVS(boolean VS) {
            this.VS = VS;
        }

        public boolean isM_CPPA() {
            return M_CPPA;
        }

        public void setM_CPPA(boolean m_CPPA) {
            M_CPPA = m_CPPA;
        }

        public boolean isM_CPP() {
            return M_CPP;
        }

        public void setM_CPP(boolean m_CPP) {
            M_CPP = m_CPP;
        }
    }
}
