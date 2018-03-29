package com.yzl.ciger.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.event.LibModuleEvents;

import org.greenrobot.eventbus.EventBus;


/**
 * 微信支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ToastUtils.showShort("开启微信支付");
        //通过WXAPIFactory工厂，获取IWXApi的实例
        api = WXAPIFactory.createWXAPI(this, LibConstant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {}

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
//            ToastUtils.showShort("微信支付完成");
            EventBus.getDefault().post(LibModuleEvents.getIntence().new WXPayEvent(true));
        } else {
            EventBus.getDefault().post(LibModuleEvents.getIntence().new WXPayEvent(false));
        }
        finish();
    }
}