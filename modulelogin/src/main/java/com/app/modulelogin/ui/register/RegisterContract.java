package com.app.modulelogin.ui.register;


import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;
import com.app.modulelogin.params.RegisterParams;

public interface RegisterContract {

    interface RegisterView extends BaseView {

        void getRegisterParams(RegisterParams params);
    }

    abstract class RegisterPresenter<T> extends BasePresent<T> {

        public abstract void onLookAgreementListener();

        public abstract void registerListener();

    }
}
