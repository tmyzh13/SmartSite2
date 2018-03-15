package com.isoftstone.smartsite.http.video;

import java.io.Serializable;

/**
 * Created by gone on 2017/10/29.
 */

public class DevicesBean implements Serializable{

    private static final long serialVersionUID = 0x0001L;

    private  DevicesArch arch;
    private int cameraType;   //摄像头类型   1固定摄像头   2云台摄像机   3 高清固定摄像头   4高清云台摄像机
    //5车载摄像机   6不可控标清摄像机    7不可控高清摄像机
    private String deviceId;		//设备ID
    private String deviceCoding	;	//设备编码
    private String deviceName;		//设备名称
    private Integer deviceType;		//设备类型（0为PM10，1为监控）
    private String installTime;	             //Date(yyyy-MM-dd HH:mm:ss)	安装时间
    private String deviceStatus;		  //设备状态（0在线，1离线，2故障）
    private String longitude;	      //设备精度
    private String  latitude;		//设备纬度


    public void DevicesBean(String deviceCoding, String deviceName,int deviceType, String deviceStatus, int cameraType) {
        this.deviceCoding = deviceCoding;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceStatus = deviceStatus;
        this.cameraType = cameraType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCoding() {
        return deviceCoding;
    }

    public void setDeviceCoding(String deviceCoding) {
        this.deviceCoding = deviceCoding;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public DevicesArch getArch() {
        return arch;
    }

    public void setArch(DevicesArch arch) {
        this.arch = arch;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public static class DevicesArch implements Serializable{
        private static final long serialVersionUID = 0x0002L;

        private String id;
        private String parentId ;
        private String  parentName;
        private String  level;
        private String  name;
        private String  number;
        private String  searchCode;
        private String  creatorId;
        private String  hasChild;
        private String  longitude;
        private String  latitude;
        private String  createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSearchCode() {
            return searchCode;
        }

        public void setSearchCode(String searchCode) {
            this.searchCode = searchCode;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getHasChild() {
            return hasChild;
        }

        public void setHasChild(String hasChild) {
            this.hasChild = hasChild;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    @Override
    public String toString() {
        return "DevicesBean{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceCoding='" + deviceCoding + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType=" + deviceType +
                ", installTime='" + installTime + '\'' +
                ", arch=" + arch +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", cameraType=" + cameraType +
                '}';
    }
}
