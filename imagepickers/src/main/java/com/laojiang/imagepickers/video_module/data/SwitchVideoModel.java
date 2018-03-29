package com.laojiang.imagepickers.video_module.data;

/**
 * 可切换清晰度视频数据bean
 */

public class SwitchVideoModel extends VideoModel {
    //清晰度标签
    private String name;

    public SwitchVideoModel(String name, String url) {
        super(url);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}