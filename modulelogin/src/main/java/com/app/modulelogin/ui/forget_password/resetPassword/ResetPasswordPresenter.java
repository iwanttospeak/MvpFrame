package com.app.modulelogin.ui.forget_password.resetPassword;

import android.support.v4.util.ArrayMap;

import com.alibaba.android.arouter.utils.TextUtils;
import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.http.param_convert.ConvertRequestParams;
import com.app.modulelogin.LoginApiService;
import com.app.modulelogin.R;
import com.app.modulelogin.params.ResetPassParams;
import com.yzl.appres.constant.LibConstant;

import java.util.Map;


public class ResetPasswordPresenter extends ResetPasswordContract.ForgetPasswordPresenter<ResetPasswordContract.ForgetPasswordView> {

    private ResetPassParams params;
    @Override
    public void start() {
        params = new ResetPassParams();
    }

    /**
     * 重置密码.
     */
    @Override
    public void onResetPasswordListener() {
        mView.setParams(params);
        if (!checkCanInput()) return;
        requestChangePassword();
    }

    private boolean checkCanInput() {


        if (TextUtils.isEmpty(params.getPassword())) {
            showToast(R.string.login_register_password_hint);
            return false;
        }
        if (TextUtils.isEmpty(params.getConfirmPass())) {
            showToast(R.string.login_register_confirm_password_hint);
            return false;
        }
        if (!params.getConfirmPass().equals(params.getPassword())) {
            showToast(R.string.login_register_pass_no_hint);
            return false;
        }
        return true;
    }

    /**
     * 修改密码.
     */
    private void requestChangePassword() {
        Map<String, String> paramsMap = ConvertRequestParams.moduleToRequestParams(params);
        mNetRequest.requestNoDataWithDialog(getApiService(LoginApiService.class).forgetPwdStep2(paramsMap), data -> {
            showToast(R.string.login_forget_password_login_success);
            mActivity.finish();
        }, new ErrorBack() {
            @Override
            public void onFailure(Object errorData) {

            }
        },true);
    }

}
