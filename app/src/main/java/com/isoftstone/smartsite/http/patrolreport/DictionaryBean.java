package com.isoftstone.smartsite.http.patrolreport;

/**
 * Created by gone on 2017/11/6.
 */

public class DictionaryBean {
    private String content;
    private String value;
    private String code;
    private String id;
    private String lang;
    private String category;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "content:" + content + " value" + value + " code" + code + " id" + id + " lang" + lang + " category:" + category;
    }
}
