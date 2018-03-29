package com.yzl.appres.pay;

import android.app.Activity;

import com.app.baselib.Utils.GsonUtils;
import com.app.baselib.constant.BaseConstants;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.pay.listener.IPayBean;
import com.yzl.appres.pay.model.AlpayBean;
import com.yzl.appres.pay.model.WxPayBean;


/**
 * 支付工厂类
 * @author 沈小建 on 2016/12/5 0005.
 */
public class PayFactory {

    public static IPayBean createPay(int type, Activity activity, String content) {
        switch (type) {
            case LibConstant.BALANCE:
                return new BalancePay();

            case LibConstant.WECHAT:
                WxPayBean wxPayBean = GsonUtils.getObject(content, WxPayBean.class);
                return new WXPay(activity, wxPayBean);

            case LibConstant.ALIPAY:
                AlpayBean payModel = GsonUtils.getObject(content, AlpayBean.class);
                return new ALPay(activity, payModel);
        }
        return null;
    }

}
