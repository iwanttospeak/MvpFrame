package com.yzl.moduleforum.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * @author by Wang
 */
public class ReleaseDynamicParams extends BaseParams{

    //标题
    @FieldProp()
    private String title;
    //内容
    @FieldProp()
    private String content;
    //图片（中间用,隔开）
    @FieldProp()
    private String images;
    //是否发布（1：发布 0：保存草稿箱）
    @FieldProp()
    private String is_publish;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }
}
