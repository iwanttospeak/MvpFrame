package com.app.baselib.http.params;


import com.app.baselib.annotation.FieldProp;

/**
 *
 * 所有网络请求参数的基类,字段可为空
 */
public class BaseParams   {

    @FieldProp()
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
