package com.app.modulelogin.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.baselib.BaseApplication;
import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.http.param_convert.ConvertRequestParams;
import com.app.modulelogin.LoginApiService;
import com.app.modulelogin.R;
import com.app.modulelogin.bean.QqLoginBean;
import com.app.modulelogin.params.LoginParams;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yzl.appres.bean.LoginBean;
import com.yzl.appres.bean.PersonalInfoBean;
import com.yzl.appres.constant.Global;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.router.AppSkip;
import com.yzl.appres.router.LoginSkip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;


public class LoginPresenter extends LoginContract.LoginPresenter<LoginContract.LoginView> {

    private Tencent mTencent;
    private IUiListener LoginListener = new LoginListener();

    private LoginParams loginParams;
    @Override
    public void start() {
        loginParams = new LoginParams();
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            mTencent.onActivityResultData(requestCode, resultCode, data, LoginListener);
        }
    }

    @Override
    public void loginListener() {
        loginParams.setPhone(mView.getAccount());
        loginParams.setPasswd(mView.getPassword());

        if (checkCanLogin(loginParams.getPhone(), loginParams.getPhone())) requestLogin();
    }

    /**
     * 检测是否可以登录.
     */
    private boolean checkCanLogin(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            showToast(R.string.login_login_phone_hint);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(R.string.login_login_password_hint);
            return false;
        }

        return true;
    }

    /**
     * 登陆（网络请求）.
     */
    private void requestLogin() {
        loginParams.setJpush_token(JPushInterface.getRegistrationID(mContext));
        Map<String, String> params = ConvertRequestParams.moduleToRequestParams(loginParams);

        mNetRequest.requestWithDialog(getApiService(LoginApiService.class).login(params), data -> requestLoginSuccess(data), true);
    }

    private void requestLoginSuccess(LoginBean bean) {
        Global.saveLogin(true);
        Global.saveToken(bean.getCustomer().getToken());
        Global.saveLoginBean(bean);
        ARouterUtil.goActivity(AppSkip.HOME_ACTIVITY);
        mActivity.finish();
    }

    /**
     * 跳转到注册页面.
     */
    @Override
    public void goRegisterListener() {
        ARouterUtil.goActivity(LoginSkip.LOGIN_REGISTER);
    }

    /**
     * 跳到忘记密码页面 .
     */
    @Override
    public void onForgetPasswordListener() {
        Bundle bundle = new Bundle();
        bundle.putString("title", mContext.getString(R.string.login_login_forget_password));
        ARouterUtil.goActivity(LoginSkip.FORGET_PASSOWRD, bundle);
    }

    /**
     * QQ登陆.
     */
    @Override
    public void qqLoginListener() {
        mTencent = Tencent.createInstance("1105462469", mActivity);
        mTencent.login(mActivity, "all", LoginListener);
    }

    /**
     * qq回调.
     */
    public class LoginListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = (JSONObject) o;
            try {
                requestLoginQQ(jsonObject.getString("openid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            showToast("登陆失败");
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消登录", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * qq登陆.
     */
    private void requestLoginQQ(String openid) {
        Map<String, String> params = new ArrayMap<>();
        params.put("oauth", "Qq");
        params.put("openid", openid);
        mNetRequest.requestWithDialog(getApiService(LoginApiService.class).qqLogin(params), new CallBack<QqLoginBean>() {
            @Override
            public void onResponse(QqLoginBean data) {
                PersonalInfoBean content = data.getContent();

                if (!content.isResult()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("qq", data);
                    ARouterUtil.goActivity(LoginSkip.THIRD_PART_PERFECT, bundle);
                    mActivity.finish();
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
                QqLoginBean bean = (QqLoginBean) errorData;
                Toast.makeText(mContext, bean.getMessage(), Toast.LENGTH_SHORT).show();
                BaseApplication application = ((BaseApplication) mActivity.getApplication());
                //Todo
//                application.destroyAllActivity();
            }
        }, true);
    }

    /**
     * 微信登陆事件.
     */
    @Override
    public void onChatLoginListener() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";

        IWXAPI api = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), LibConstant.WX_APP_ID, true);
        api.sendReq(req);
    }
}
