package com.atgc.cotton.entity;

/**
 * Created by GMARUnity on 2017/6/26.
 */
public class VendUploadEntity {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String title = "";
    private String content = "";
}
