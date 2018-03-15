package com.isoftstone.smartsite.http.muckcar;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/18.
 */

public class EvidencePhotoBeanPage extends PageBean{
    private ArrayList<EvidencePhotoBean> content;

    public ArrayList<EvidencePhotoBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<EvidencePhotoBean> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EvidencePhotoBeanPage{" +
                "first=" + first +
                ", last=" + last +
                ", number=" + number +
                ", numberOfElements=" + numberOfElements +
                ", content size =" + content.size() +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
