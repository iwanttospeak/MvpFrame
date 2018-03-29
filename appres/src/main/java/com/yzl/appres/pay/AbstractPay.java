package com.yzl.appres.pay;

import android.content.Context;
import android.content.Intent;

import com.yzl.appres.bean.PayBean;


/**
 * Created by 沈小建 on 2018-01-16.
 */

public abstract class AbstractPay {

    protected Context mContext;
    protected PayBean mPayBean;
    protected OnPaySuccessListener mOnPaySuccessListener;

    public AbstractPay(Context context, PayBean payBean) {
        mContext = context;
        mPayBean = payBean;

    }

    public void setOnPaySuccessListener(OnPaySuccessListener onPaySuccessListener) {
        mOnPaySuccessListener = onPaySuccessListener;
    }

    /**
     * 回调.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 处理支付.
     */
    public abstract void disposePay();

    public interface OnPaySuccessListener {
        void onSuccess();
    }
}
