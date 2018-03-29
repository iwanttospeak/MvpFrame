package com.laojiang.imagepickers.video_module.data;

/**
 * 视频数据bean
 */
public class VideoModel {
    private String url;

    public VideoModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}