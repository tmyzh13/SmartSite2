package com.isoftstone.smartsite.http.message;

import com.isoftstone.smartsite.http.pageable.PageableBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/12/2.
 */

public class MessagePage extends PageableBean {
    private ArrayList<MessageBean> content;

    public ArrayList<MessageBean> getContent() {
        return content;
    }
}
