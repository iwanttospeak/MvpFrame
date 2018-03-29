package com.app.modulelogin.ui.forget_password;


import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;
import com.app.modulelogin.params.ResetPassParams;

public interface ForgetPasswordContract {

    public interface ForgetPasswordView extends BaseView {

        public void setCodeType(int type);
        void setParams(ResetPassParams params);

    }

    public abstract class ForgetPasswordPresenter<T> extends BasePresent<T> {

        public abstract void onResetPasswordListener();
    }
}
