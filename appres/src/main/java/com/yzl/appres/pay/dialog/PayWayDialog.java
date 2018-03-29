package com.yzl.appres.pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.baselib.Utils.StringUtil;
import com.yzl.appres.R;
import com.yzl.appres.constant.LibConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 选择支付方式Dialog
 * Created by Hh on 2017/3/6.
 */
public class PayWayDialog extends Dialog {

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


    /** 是否显示钱包 **/
    private boolean isShowBalance;

    private View.OnClickListener onClickListener;

    /** 区别三种支付方式 3:我的钱包 1:支付宝 2:微信支付 **/
    public static int payWay = LibConstant.BALANCE;

    /** 如果ifRecharge 传入true 则是,就显示我的钱包, 否则则隐藏 **/
    public PayWayDialog(Context context, int themeResId, boolean isShowBalance, View.OnClickListener onClickListener) {
        super(context, themeResId);
        this.isShowBalance = isShowBalance;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_dialog_pay_info);
        ButterKnife.bind(this);
        initView();
        initListener();
        initPayState();
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = AbsListView.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
    }


    private void initView() {
        dialogBalance = (TextView) findViewById(R.id.dialog_balance);
        dialogMyWallet = (LinearLayout) findViewById(R.id.dialog_my_wallet);
        rechargeNum = (TextView) findViewById(R.id.recharge_num);
        dialogConfirmPay = (TextView) findViewById(R.id.dialog_confirm_pay);
        closeImage = (ImageView) findViewById(R.id.recharge_dialog_close);
        checks = new ArrayList<>();
        ballanceHint = (ImageView) findViewById(R.id.recharge_wallet_cb);
        weixinHint = (ImageView) findViewById(R.id.recharge_wechat_cb);
        aliHint = (ImageView) findViewById(R.id.recharge_zhifubao_cb);
        checks.add(ballanceHint);
        checks.add(weixinHint);
        checks.add(aliHint);
    }
    private void initListener() {
        /* 关闭 **/
        closeImage.setOnClickListener(view -> dismiss());
        /* 我的钱包 **/
        dialogMyWallet.setOnClickListener(view -> checkChanges(LibConstant.BALANCE));
        /* 微信 **/
        findViewById(R.id.dialog_wechat).setOnClickListener(view -> checkChanges(LibConstant.WECHAT));
        /* 支付宝 **/
        findViewById(R.id.dialog_alipay).setOnClickListener(view -> checkChanges(LibConstant.ALIPAY));
        dialogConfirmPay.setOnClickListener(onClickListener);

    }
    /**
     * 初始化显示状态
     */
    private void initPayState() {
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_no_select));
        }
        if (isShowBalance) {//充值
            dialogMyWallet.setVisibility(View.VISIBLE);
            payWay = LibConstant.BALANCE;
            ballanceHint.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_select));
        }else {
            dialogMyWallet.setVisibility(View.GONE);
            payWay = LibConstant.ALIPAY;
            aliHint.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_select));
        }
    }
    /** 设置充值金额 **/
    public void setRechargeNum(String num, String balance) {
        rechargeNum.setText("￥ " + StringUtil.numberFormat(Double.parseDouble(num)));
        if (!isShowBalance) {
            dialogBalance.setText(balance);
        }
    }
    /** 改变选中 **/
    private void checkChanges(int payType) {
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_no_select));
        }
        switch (payType){
            case LibConstant.BALANCE:
                payWay = LibConstant.BALANCE;
                ballanceHint.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_select));
                break;
            case LibConstant.WECHAT:
                payWay = LibConstant.WECHAT;
                weixinHint.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_select));
                break;
            case LibConstant.ALIPAY:
                payWay = LibConstant.ALIPAY;
                aliHint.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_no_select));
                break;
        }
    }
}
