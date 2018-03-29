package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * @author by Wang on 2017/7/8.
 */

public class RegisterParams extends BaseParams {
    //电话
    @FieldProp()
    private String phone = "" ;
    //密码
    @FieldProp()
    private String password ="";
    //密码
    @FieldProp()
    private String confirmPassword ="";
    //昵称
    @FieldProp()
    private String nickname="";
    //验证码
    @FieldProp()
    private String jpush_token = "" ;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getJpush_token() {
        return jpush_token;
    }

    public void setJpush_token(String jpush_token) {
        this.jpush_token = jpush_token;
    }
}
