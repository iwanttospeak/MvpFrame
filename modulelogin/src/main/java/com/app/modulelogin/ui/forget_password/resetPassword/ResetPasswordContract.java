package com.app.modulelogin.ui.forget_password.resetPassword;


import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;
import com.app.modulelogin.params.ResetPassParams;

public interface ResetPasswordContract {

    public interface ForgetPasswordView extends BaseView {

        void setParams(ResetPassParams params);

    }

    public abstract class ForgetPasswordPresenter<T> extends BasePresent<T> {

        public abstract void onResetPasswordListener();
    }
}
