package com.app.modulelogin.ui.register.register_code;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.mvp.BaseActivity;
import com.app.baselib.widget.SendCodeTV;
import com.app.modulelogin.R;
import com.app.modulelogin.R2;
import com.app.modulelogin.params.RegisterCodeParams;
import com.yzl.appres.router.LoginSkip;

import butterknife.BindView;


@Route(path = LoginSkip.LOGIN_REGISTER_CODE)
public class RegisterCodeActivity extends BaseActivity<RegisterCodePresenter> implements RegisterContract.RegisterView {

    @BindView(R2.id.et_phone)
    EditText mEtPhone;

    @BindView(R2.id.et_code)
    EditText mEtCode;

    @BindView(R2.id.tv_code)
    SendCodeTV mTvCode;
    private TextView tvRight;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity_register_code;
    }

    @Override
    public void setCodeType(int type) {
        mTvCode.setEtPhone(mEtPhone);
        mTvCode.setType(type);
    }

    @Override
    public void setParams(RegisterCodeParams params) {
        params.setPhone(mEtPhone.getText().toString());
        params.setVerify_code(mEtCode.getText().toString());
    }

    @Override
    public void init() {
        tvRight = findViewById(R.id.tv_right);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.onCodeRegister();
            }
        });
        setToolTitle("验证手机号");
        setRightTitle("下一步");
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
