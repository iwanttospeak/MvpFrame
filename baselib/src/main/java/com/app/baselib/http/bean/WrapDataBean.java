package com.app.baselib.http.bean;


/**
 * 网络请求返回数据包装类
 */
public class WrapDataBean<T> extends WrapDataNoContent<T> {

    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
