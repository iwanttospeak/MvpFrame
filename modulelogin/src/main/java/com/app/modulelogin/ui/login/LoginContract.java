package com.app.modulelogin.ui.login;

import android.content.Intent;

import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;


public interface LoginContract {

    public interface LoginView extends BaseView {

        public String getAccount();

        public String getPassword();

    }

    public abstract class LoginPresenter<T> extends BasePresent<T> {

        public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

        public abstract void loginListener();

        public abstract void onForgetPasswordListener();

        public abstract void goRegisterListener();

        public abstract void qqLoginListener();

        public abstract void onChatLoginListener();
    }
}
