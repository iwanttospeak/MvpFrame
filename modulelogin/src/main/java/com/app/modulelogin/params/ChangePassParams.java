package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * 修改密码
 * @author by Wang on 2017/7/16.
 */

public class ChangePassParams extends BaseParams {

    //旧密码
    @FieldProp()
    private String old_pwd = "" ;
    //新密码
    @FieldProp()
    private String now_pwd ="";

    //确认密码
    @FieldProp()
    private String confirm_pwd ="";

    //修改密码
    @FieldProp()
    private String phone ="";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConfirm_pwd() {
        return confirm_pwd;
    }

    public void setConfirm_pwd(String confirm_pwd) {
        this.confirm_pwd = confirm_pwd;
    }

    public String getOld_pwd() {
        return old_pwd;
    }

    public void setOld_pwd(String old_pwd) {
        this.old_pwd = old_pwd;
    }

    public String getNow_pwd() {
        return now_pwd;
    }

    public void setNow_pwd(String now_pwd) {
        this.now_pwd = now_pwd;
    }
}
