package com.app.modulelogin.params;


import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 */
public class LoginParams extends BaseParams {
    //电话
    @FieldProp()
    private String phone = "" ;
    //密码
    @FieldProp()
    private String password ="";
    //登录方式
    @FieldProp()
    private String oauth="";
    //极光推送
    @FieldProp()
    private String jpush_token="";

    public String getJpush_token() {
        return jpush_token;
    }

    public void setJpush_token(String jpush_token) {
        this.jpush_token = jpush_token;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return password;
    }

    public void setPasswd(String passwd) {
        this.password = passwd;
    }


}

