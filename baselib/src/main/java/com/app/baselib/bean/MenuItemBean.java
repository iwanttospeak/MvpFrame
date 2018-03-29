package com.app.baselib.bean;

/**
 * @author by Wang on 2017/12/25.
 */

public class MenuItemBean {

    private int imgResource;

    private String itemContent;

    public MenuItemBean(String content, int imgResource){
        this.imgResource = imgResource;
        itemContent = content;
    }
    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
}
