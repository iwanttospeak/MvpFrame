package com.app.modulelogin.params;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

/**
 * @author by Wang
 */

public class ResetPassParams extends BaseParams {

    @FieldProp()
    private String confirmPass = "" ;
    @FieldProp()
    private String password ="";
    @FieldProp()
    private String phone = "" ;
    @FieldProp()
    private String verify_code ="";

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
