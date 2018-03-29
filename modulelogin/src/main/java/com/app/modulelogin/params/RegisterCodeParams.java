package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * @author by Wang on 2017/7/8.
 */

public class RegisterCodeParams extends BaseParams {
    //电话
    @FieldProp()
    private String phone = "" ;
    //电话
    @FieldProp()
    private String verify_code = "" ;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }
}
