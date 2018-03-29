package com.app.modulelogin.ui.register.register_code;


import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;
import com.app.modulelogin.params.RegisterCodeParams;

public interface RegisterContract {

    public interface RegisterView extends BaseView {

        public void setCodeType(int type);

        void setParams(RegisterCodeParams params);
    }

    public abstract class RegisterPresenter<T> extends BasePresent<T> {

        public abstract void onCodeRegister();
    }
}
