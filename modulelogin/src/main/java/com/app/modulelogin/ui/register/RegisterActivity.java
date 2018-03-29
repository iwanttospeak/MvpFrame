package com.app.modulelogin.ui.register;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.mvp.BaseActivity;
import com.app.baselib.widget.SendCodeTV;
import com.app.modulelogin.R;
import com.app.modulelogin.R2;
import com.app.modulelogin.params.RegisterParams;
import com.yzl.appres.router.LoginSkip;

import butterknife.BindView;
import butterknife.OnClick;


@Route(path = LoginSkip.LOGIN_REGISTER)
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.RegisterView {

    @BindView(R2.id.et_nickname)
    EditText mEtName;

    @BindView(R2.id.et_password)
    EditText mEtPassword;

    @BindView(R2.id.tv_agreement)
    TextView mTvAgreement;

    @BindView(R2.id.et_confirm_password)
    EditText mEtConfirmPassword;

    private TextView tvRight;
    @Override
    public void init() {
        setToolTitle("注册");
        tvRight = findViewById(R.id.tv_right);
        tvRight.setText("完成");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.registerListener();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_activity_register;
    }


    @Override
    public void getRegisterParams(RegisterParams params) {
        params.setNickname(mEtConfirmPassword.getText().toString());
        params.setNickname(mEtName.getText().toString());
        params.setPassword(mEtPassword.getText().toString());
    }



    /**
     * 查看协议.
     */
    @OnClick(R2.id.tv_agreement)
    public void onLookAgreementListener() {
        mPresent.onLookAgreementListener();
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
