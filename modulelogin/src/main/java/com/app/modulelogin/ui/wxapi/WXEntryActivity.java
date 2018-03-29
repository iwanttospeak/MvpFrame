package com.app.modulelogin.ui.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.app.baselib.BaseApplication;
import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.http.NetRequest;
import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.app.modulelogin.bean.WeiXinLoginBean;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.tools.Constant;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.app.baselib.http.Api;
import com.app.modulelogin.LoginApiService;
import com.yzl.appres.bean.PersonalInfoBean;
import com.yzl.appres.constant.Global;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.router.AppSkip;
import com.yzl.appres.router.LoginSkip;

import java.util.Map;

import io.reactivex.Observable;

/**
 *
 * Created by shen on 2017-06-06.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    /**
     * IWXAPI 是第三方app和微信通信的openapi接口.
     */
    private IWXAPI api;

    /**
     * 时间戳转换为毫秒.
     */
    private static final int TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, LibConstant.WX_APP_ID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        //登录回调,处理登录成功的逻辑
                        String code = ((SendAuth.Resp) baseResp).code;
                        requestLogin(code);
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //分享回调
                        finish();
                        break;
                    default:
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
        }
    }

    /**
     * 登陆.
     */
    private void requestLogin(String code) {
        Map<String, String> params = new ArrayMap<>();
        params.put("oauth", "Weixin");
        params.put("code", code);
        NetRequest netRequest = new NetRequest(this);
        Observable<WrapDataBean<WeiXinLoginBean>> observable =Api.getInstance().getApiService(LoginApiService.class).weiXinLogin(params);
        netRequest.requestWithDialog(observable, new CallBack<WeiXinLoginBean>() {
            @Override
            public void onResponse(WeiXinLoginBean data) {
                PersonalInfoBean content = data.getContent();
                if (!content.isResult()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("weixin", data);
                    ARouterUtil.goActivity(LoginSkip.THIRD_PART_PERFECT, bundle);
                    finish();
                    return;
                }
                Global.saveLogin(true);
                Global.saveToken(content.getAccess_token());
                Global.savePersonalInfo(content);

                ARouterUtil.goActivity(AppSkip.HOME_ACTIVITY);
            }
        }, new ErrorBack() {
            @Override
            public void onFailure(Object errorData) {
                WrapDataBean<WeiXinLoginBean> loginBean = (WrapDataBean<WeiXinLoginBean>) errorData;
                ToastUtils.showShort(loginBean.getMessage());
                ((BaseApplication) getApplication()).destroyAllActivity();
            }
        }, true);
    }
}