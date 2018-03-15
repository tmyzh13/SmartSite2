package com.isoftstone.smartsite.http.message;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/12/8.
 */

public class MessageBeanPage extends PageBean{
    ArrayList<MessageBean> content;

    public ArrayList<MessageBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<MessageBean> content) {
        this.content = content;
    }
}
