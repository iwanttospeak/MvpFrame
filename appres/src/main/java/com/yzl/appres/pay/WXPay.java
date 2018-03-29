package com.yzl.appres.pay;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.pay.listener.IPayBean;
import com.yzl.appres.pay.model.WxPayBean;

/**
 * 微信支付JavaBean
 * Created by ADongdpa on 16/5/19.
 */
public class WXPay extends IPayBean {

    private IWXAPI api;
    private Context mContext;
    private WxPayBean payModel;

    public WXPay(Context context, WxPayBean payModel) {
        mContext = context;
        this.payModel = payModel;
        wxPay();
    }

    private void wxPay() {
        api = WXAPIFactory.createWXAPI(mContext, LibConstant.WX_APP_ID);
        api.registerApp(payModel.getAppid());

        PayReq req = new PayReq();
        req.appId = payModel.getAppid();
        req.partnerId = payModel.getPartnerid();
        req.prepayId = payModel.getPrepayid();
        req.packageValue = payModel.getPackage_name();
        req.nonceStr = payModel.getNoncestr();
        req.timeStamp = payModel.getTimestamp();
        req.sign = payModel.getSign();
        api.sendReq(req);
//        ToastUtils.showShort("发送微信支付请求");

    }

}
