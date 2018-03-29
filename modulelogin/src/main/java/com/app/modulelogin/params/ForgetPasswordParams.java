package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * 忘记密码
 * @author by Wang on 2017/7/16.
 */

public class ForgetPasswordParams extends BaseParams {

    //电话
    @FieldProp()
    private String phone = "" ;
    //密码
    @FieldProp()
    private String passwd ="";
    //确认密码
    @FieldProp()
    private String confirm_passwd="";
    //验证码
    @FieldProp()
    private String verify ="" ;
    //验证码类型
    @FieldProp()
    private String type ="";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getConfirm_passwd() {
        return confirm_passwd;
    }

    public void setConfirm_passwd(String confirm_passwd) {
        this.confirm_passwd = confirm_passwd;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
