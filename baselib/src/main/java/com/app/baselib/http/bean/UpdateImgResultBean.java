package com.app.baselib.http.bean;

/**
 * @author by Wang on 2018/1/16.
 * 上传图片返回数据bean
 */
public class UpdateImgResultBean {
//    {
//            w:400, //图片宽度，七牛会返回
//            h:800,//图片高度，七牛返回
//            name:"",//图片key名
//            key:"",
//            url:"",//图片url网址
//    }
    private String w;
    private String h;
    private String url;
    private String key;
    private String name;

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
