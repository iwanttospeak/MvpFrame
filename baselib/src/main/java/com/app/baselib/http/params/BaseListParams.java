package com.app.baselib.http.params;

import com.app.baselib.annotation.FieldProp;

/**
 * 分页加载的基础请求参数
 * @author by Wang on 2017/8/15.
 */

public class BaseListParams extends BaseParams{

    //token
    @FieldProp()
    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
