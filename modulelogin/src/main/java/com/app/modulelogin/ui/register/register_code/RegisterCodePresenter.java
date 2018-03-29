package com.app.modulelogin.ui.register.register_code;

import android.os.Bundle;

import com.alibaba.android.arouter.utils.TextUtils;
import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.http.param_convert.ConvertRequestParams;
import com.app.modulelogin.LoginApiService;
import com.app.modulelogin.R;
import com.app.modulelogin.params.RegisterCodeParams;
import com.yzl.appres.router.LoginSkip;

import java.util.Map;
import io.reactivex.Observable;


public class RegisterCodePresenter extends RegisterContract.RegisterPresenter<RegisterContract.RegisterView> {


    private RegisterCodeParams codeParams;
    @Override
    public void start() {
        codeParams = new RegisterCodeParams();
    }

    /**
     * 重置密码.
     */
    @Override
    public void onCodeRegister() {
        if (!checkCanInput()) return;
        requestCode();
    }

    private boolean checkCanInput() {
        mView.setParams(codeParams);
        if (TextUtils.isEmpty(codeParams.getPhone())) {
            showToast(R.string.login_change_phone_phone_hint);
            return false;
        }
        if (TextUtils.isEmpty(codeParams.getVerify_code())) {
            showToast(R.string.login_change_phone_code_hint);
            return false;
        }
        return true;
    }

    /**
     *
     */
    private void requestCode() {
        Map<String, String> params = ConvertRequestParams.moduleToRequestParams(codeParams);
        mNetRequest.requestWithDialog(getApiService(LoginApiService.class).resetPassword(params), data -> {
            perfectListener();
        }, true);
    }
    /**
     * 进完善信息.
     */
    public void perfectListener() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", codeParams.getPhone());
        bundle.putString("code", codeParams.getVerify_code());
        ARouterUtil.goActivity(LoginSkip.LOGIN_REGISTER, bundle);
    }
}
