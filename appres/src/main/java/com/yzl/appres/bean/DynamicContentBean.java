package com.yzl.appres.bean;

/**
 * @author by Wang on 2018/1/12.
 * 动态内容数据bean
 */

public class DynamicContentBean {

    private String imgUrl;

    private String type;

    private String videoPath;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
