package com.app.modulelogin.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.http.param_convert.ConvertRequestParams;
import com.app.baselib.widget.WebActivity;
import com.app.modulelogin.LoginApiService;
import com.app.modulelogin.R;
import com.app.modulelogin.constant.LoginConstant;
import com.app.modulelogin.params.RegisterParams;
import com.blankj.utilcode.util.RegexUtils;
import com.yzl.appres.bean.LoginBean;
import com.yzl.appres.constant.Global;
import com.yzl.appres.router.LoginSkip;

import java.util.Map;


public class RegisterPresenter extends RegisterContract.RegisterPresenter<RegisterContract.RegisterView> {


    private RegisterParams registerParams;
    @Override
    public void start() {
        registerParams = new RegisterParams();
    }

    /**
     * 查看协议.
     */
    @Override
    public void onLookAgreementListener() {
        Bundle bundle = new Bundle();
        bundle.putString("title", mContext.getString(R.string.login_register_agreement));
        bundle.putString("url", Global.getAgreement());
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 注册事件.
     */
    @Override
    public void registerListener() {
        if (!checkAll()) return;
        Map<String, String> params = ConvertRequestParams.moduleToRequestParams(registerParams);
        requestRegister(params);
    }

    /**
     * 获取数据并检测.
     */
    private boolean checkAll() {
        mView.getRegisterParams(registerParams);
        if (!checkPhone(registerParams.getPhone())) return false;
        if (!checkPassword()) return false;
        return true;
    }

    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.login_register_phone);
            return false;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            showToast(R.string.login_register_correct_phone);
            return false;
        }
        return true;
    }

    private boolean checkPassword( ) {
        if (TextUtils.isEmpty(registerParams.getPassword())) {
            showToast(R.string.login_register_password_hint);
            return false;
        }
        if (TextUtils.isEmpty(registerParams.getConfirmPassword())) {
            showToast(R.string.login_register_confirm_password_hint);
            return false;
        }
        if (!registerParams.getPassword().equals(registerParams.getConfirmPassword())) {
            showToast(R.string.login_register_pass_no_hint);
            return false;
        }
        return true;
    }
    /**
     * 注册（网络）.
     */
    private void requestRegister(Map<String, String> params) {
        mNetRequest.requestWithDialog(getApiService(LoginApiService.class).register(params), data -> requestRegisterSuccess(data), true);
    }
    private void requestRegisterSuccess(LoginBean bean) {
        Global.saveLogin(true);
        Global.saveLoginBean(bean);
        Global.saveToken(bean.getCustomer().getToken());

        Bundle bundle = new Bundle();
        bundle.putBoolean("email", false);
        ARouterUtil.goActivity(LoginSkip.LOGIN_REGISTER_RESULT, bundle);
    }
}
