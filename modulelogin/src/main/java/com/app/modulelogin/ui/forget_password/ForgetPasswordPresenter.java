package com.app.modulelogin.ui.forget_password;

import android.support.v4.util.ArrayMap;

import com.alibaba.android.arouter.utils.TextUtils;
import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.http.param_convert.ConvertRequestParams;
import com.app.modulelogin.LoginApiService;
import com.app.modulelogin.R;
import com.app.modulelogin.params.ResetPassParams;
import com.luck.picture.lib.tools.Constant;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.router.LoginSkip;

import java.util.Map;


public class ForgetPasswordPresenter extends ForgetPasswordContract.ForgetPasswordPresenter<ForgetPasswordContract.ForgetPasswordView> {

    private int type;

    private ResetPassParams params;
    @Override
    public void start() {
        params = new ResetPassParams();

        boolean isChangePassword = mActivity.getIntent().getBooleanExtra("change", false);
        type = isChangePassword ? LibConstant.CODE_TYPE_CHANGE_PASSWORD : LibConstant.CODE_TYPE_FIND_PASSOWRD;
        mView.setCodeType(type);
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

        if (TextUtils.isEmpty(params.getPhone())) {
            showToast(R.string.login_change_phone_phone_hint);
            return false;
        }
        if (TextUtils.isEmpty(params.getVerify_code())) {
            showToast(R.string.login_change_phone_code_hint);
            return false;
        }
        return true;
    }

    /**
     * 修改密码.
     */
    private void requestChangePassword() {
        Map<String, String> paramsMap = ConvertRequestParams.moduleToRequestParams(params);
        mNetRequest.requestWithDialog(getApiService(LoginApiService.class).resetPassword(paramsMap), data -> {
            ARouterUtil.goActivity(LoginSkip.FORGET_RESET_PASSOWRD);
        }, true);
    }

}
