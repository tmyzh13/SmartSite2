package com.isoftstone.smartsite.http.video;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/12/8.
 */

public class DevicesBeanPage extends PageBean{
    ArrayList<DevicesBean>  content;

    public ArrayList<DevicesBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<DevicesBean> content) {
        this.content = content;
    }
}
