package com.app.modulelogin.ui.login;

import android.content.Intent;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.mvp.BaseActivity;
import com.app.modulelogin.R;
import com.app.modulelogin.R2;
import com.yzl.appres.router.LoginSkip;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = LoginSkip.LOGIN_LOGIN)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {

    /**
     * 账号.
     */
    @BindView(R2.id.et_account)
    EditText mEtAccount;

    /**
     * 密码.
     */
    @BindView(R2.id.et_password)
    EditText mEtPassword;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity_login;
    }

    @Override
    public void init() {
        setToolTitle("账号登录");
    }

    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresent.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 登陆
     */
    @OnClick(R2.id.bt_login)
    public void loginListener() {
        mPresent.loginListener();
    }

    /**
     * 忘记密码事件.
     */
    @OnClick(R2.id.tv_forget_password)
    public void onForgetPasswordListener() {
        mPresent.onForgetPasswordListener();
    }

    /**
     * 前往注册页面.
     */
    @OnClick(R2.id.tv_register)
    public void goRegisterListener() {
        mPresent.goRegisterListener();
    }

    /**
     * qq登陆.
     */
    @OnClick(R2.id.tv_qq)
    public void qqLoginListener() {
        mPresent.qqLoginListener();
    }

    /**
     * 微信登录.
     */
    @OnClick(R2.id.tv_chat)
    public void onChatLoginListener() {
        mPresent.onChatLoginListener();
    }

    @Override
    public void showLoading() {
        showProcessDialog();
    }

    @Override
    public void hideLoading() {
        dismissProcessDialog();
    }
}
