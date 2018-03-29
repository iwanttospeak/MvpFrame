package com.app.modulelogin.ui.forget_password;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.mvp.BaseActivity;
import com.app.baselib.widget.SendCodeTV;
import com.app.modulelogin.R;
import com.app.modulelogin.R2;
import com.app.modulelogin.params.ResetPassParams;
import com.yzl.appres.router.LoginSkip;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = LoginSkip.FORGET_PASSOWRD)
public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.ForgetPasswordView {

    @BindView(R2.id.et_phone)
    EditText mEtPhone;

    @BindView(R2.id.et_code)
    EditText mEtCode;

    @BindView(R2.id.tv_code)
    SendCodeTV mTvCode;

    @BindView(R2.id.tv_right)
    TextView tvRight;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity_forget_password;
    }

    @Override
    public void setCodeType(int type) {
        mTvCode.setEtPhone(mEtPhone);
        mTvCode.setType(type);
    }

    @Override
    public void setParams(ResetPassParams params) {
        params.setPhone(mEtPhone.getText().toString());
        params.setVerify_code(mEtCode.getText().toString());

    }

    @Override
    public void init() {

        setToolTitle("忘记密码");
        setRightTitle("下一步");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.onResetPasswordListener();
            }
        });
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
