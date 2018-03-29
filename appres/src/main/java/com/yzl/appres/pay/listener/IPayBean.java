package com.yzl.appres.pay.listener;

/**
 * @author 沈小建 on 2016/12/5 0005.
 */
public class IPayBean {

    protected OnResultListener resultListener;

    public void setOnResultListener(OnResultListener listener) {
        resultListener = listener;
    }

    public interface OnResultListener {
        void onPaySuccess();

        void onPayFail();
    }

}
