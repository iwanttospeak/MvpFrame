package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * 修改手机号
 * @author by Wang on 2017/7/16.
 */

public class ChangePhoneParams extends BaseParams {

    //新手机
    @FieldProp()
    private String newphone;

    //验证码
    @FieldProp()
    private String newcode;

    public String getNewphone() {
        return newphone;
    }

    public void setNewphone(String newphone) {
        this.newphone = newphone;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }
}
