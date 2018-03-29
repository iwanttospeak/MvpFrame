package com.app.modulelogin.ui.forget_password.resetPassword;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.mvp.BaseActivity;
import com.app.modulelogin.R;
import com.app.modulelogin.R2;
import com.app.modulelogin.params.ResetPassParams;
import com.yzl.appres.router.LoginSkip;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = LoginSkip.FORGET_RESET_PASSOWRD)
public class ResetPasswordActivity extends BaseActivity<ResetPasswordPresenter> implements ResetPasswordContract.ForgetPasswordView {


    @BindView(R2.id.tv_right)
    TextView tvRight;
    @BindView(R2.id.et_password)
    EditText etPassword;
    @BindView(R2.id.et_confirm_password)
    EditText etConfirmPassword;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity_reset_password;
    }

    @Override
    public void init() {
        setToolTitle("忘记密码");
        setRightTitle("确认");
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

    @Override
    public void setParams(ResetPassParams params) {
        params.setPassword(etPassword.getText().toString());
        params.setConfirmPass(etConfirmPassword.getText().toString());

    }
}
