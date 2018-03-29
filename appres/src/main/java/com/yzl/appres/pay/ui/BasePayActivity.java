package com.yzl.appres.pay.ui;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.baselib.Utils.GenericityUtils;
import com.app.baselib.Utils.StringUtil;
import com.app.baselib.http.params.BaseParams;
import com.app.baselib.mvp.BaseActivity;
import com.app.baselib.mvp.BasePresent;
import com.app.baselib.widget.dialog.OnlyHintDialog;
import com.app.baselib.widget.dialog.OnlyTitleDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.yzl.appres.R;
import com.yzl.appres.constant.LibConstant;
import com.yzl.appres.event.LibModuleEvents;
import com.yzl.appres.pay.listener.IPayBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付页面
 * @author by Wang on 2017/7/27.
 */

public abstract class BasePayActivity<T extends BasePresent,E extends BaseParams> extends BaseActivity<T> {


    TextView dialogBalance;

    LinearLayout dialogMyWallet;

    List<ImageView> checks;

    //选中图标
    ImageView ballanceHint;
    //选中图标
    ImageView weixinHint;
    //选中图标
    ImageView aliHint;
    TextView rechargeNum;

    TextView dialogConfirmPay;

    ImageView closeImage;


    //请求之前显示布局
    public LinearLayout normalLayout;


    /** 是否显示钱包 **/
    private boolean isShowBalance;

    //请求支付的参数
    public E mParams;

    /** 区别三种支付方式 3:我的钱包 1:支付宝 2:微信支付 **/
    public  int payWay = LibConstant.BALANCE;

    //点击返回键弹出提示框
    private OnlyTitleDialog dialog;
    private IPayBean.OnResultListener listener;
    //是否显示dialog提示
    protected boolean isShowBackDialog = true;

    public void setShowBalance(boolean showBalance) {
        isShowBalance = showBalance;
    }
    @Override
    public int getLayoutId() {
        return R.layout.res_activity_pay;
    }

    @Override
    public void init() {
        setTitle("支付详情");
        initParams();
        initView();
        initListener();
        initPayState();
        initDialog();
    }

    protected  void initDialog(){
        dialog = new OnlyTitleDialog(this);
        dialog.setHintTitle("您要取消付款吗");
        dialog.setCancelText("狠心放弃");
        dialog.setPositiveText("继续付款");
        dialog.setOnClickListener(new OnlyTitleDialog.OnClickListener(){
            @Override
            public void onClickCancel() {
                finish();
            }
            @Override
            public void onClickPositive() {
            }
        });

    }

    /**
     * 截获返回事件
     */
    @Override
    public void backHandle() {
        KeyboardUtils.hideSoftInput(this);
        if (isShowBackDialog){
            dialog.show();
        }else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            KeyboardUtils.hideSoftInput(this);
            if (isShowBackDialog) {
                dialog.show();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        dialogBalance = (TextView) findViewById(R.id.dialog_balance);
        dialogMyWallet = (LinearLayout) findViewById(R.id.dialog_my_wallet);
        rechargeNum = (TextView) findViewById(R.id.recharge_num);
        dialogConfirmPay = (TextView) findViewById(R.id.dialog_confirm_pay);
        checks = new ArrayList<>();
        ballanceHint = (ImageView) findViewById(R.id.recharge_wallet_cb);
        weixinHint = (ImageView) findViewById(R.id.recharge_wechat_cb);
        aliHint = (ImageView) findViewById(R.id.recharge_zhifubao_cb);
        normalLayout = (LinearLayout) findViewById(R.id.ll_root);
        checks.add(ballanceHint);
        checks.add(weixinHint);
        checks.add(aliHint);
    }
    private void initListener() {

        /* 我的钱包 **/
        dialogMyWallet.setOnClickListener(view -> checkChanges(LibConstant.BALANCE));
        /* 微信 **/
        findViewById(R.id.dialog_wechat).setOnClickListener(view -> checkChanges(LibConstant.WECHAT));
        /* 支付宝 **/
        findViewById(R.id.dialog_alipay).setOnClickListener(view -> checkChanges(LibConstant.ALIPAY));

        dialogConfirmPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPay();
            }
        });
        //支付结果回调（微信）
        listener = new IPayBean.OnResultListener() {
            @Override
            public void onPaySuccess() {
                EventBus.getDefault().post(LibConstant.PAY_SUCCESS);
                successHandle();
            }
            @Override
            public void onPayFail() {
                OnlyHintDialog dialog = new OnlyHintDialog(BasePayActivity.this);
                dialog.setHintText("支付失败");
                dialog.show();
            }
        };
    }
    //微信支付回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWxPayEvent(LibModuleEvents.WXPayEvent event) {
        if (event.isSuccess()) {
            listener.onPaySuccess();
        } else {
            listener.onPayFail();
        }
    }

    /**
     * 初始化显示状态
     */
    private void initPayState() {
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).setImageResource(R.drawable.base_no_select);
        }
        if (isShowBalance) {//充值
            dialogMyWallet.setVisibility(View.VISIBLE);
            payWay = LibConstant.BALANCE;
            ballanceHint.setImageResource(R.drawable.base_select);
        }else {
            dialogMyWallet.setVisibility(View.GONE);
            payWay = LibConstant.ALIPAY;
            aliHint.setImageResource(R.drawable.base_select);
        }
    }
    /** 设置充值金额 **/
    public void setRechargeNum(String num, String balance) {
        rechargeNum.setText("￥ " + StringUtil.numberFormat(Double.parseDouble(num)));
        if (isShowBalance) {
            dialogBalance.setText(balance);
        }
    }
    /** 改变选中 **/
    private void checkChanges(int payType) {
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).setImageResource(R.drawable.base_no_select);
        }
        switch (payType){
            case LibConstant.BALANCE:
                payWay = LibConstant.BALANCE;
                ballanceHint.setImageResource(R.drawable.base_select);
                break;
            case LibConstant.WECHAT:
                payWay = LibConstant.WECHAT;
                weixinHint.setImageResource(R.drawable.base_select);
                break;
            case LibConstant.ALIPAY:
                payWay = LibConstant.ALIPAY;
                aliHint.setImageResource(R.drawable.base_select);
                break;
        }
    }

    /**
     * 付款成功处理
     */
    protected void successHandle() {

    }

    /**
     * 初始化泛型参数
     */
    protected  void initParams(){
        GenericityUtils utils = new GenericityUtils();
        Class clazz = utils.getGenericClass(this,1);
        if (clazz == null) return;
        utils.checkGenericity(clazz, getClass().getName());
        mParams = (E) utils.instantiationClass(clazz);
    }
    /**
     * 请求支付
     */
    public abstract void requestPay();
    /**
     * 请求成功页面跳转
     */
    protected abstract void successSkipActivity();
}

