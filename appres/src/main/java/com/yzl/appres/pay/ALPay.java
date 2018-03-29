package com.yzl.appres.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.alipay.sdk.app.PayTask;
import com.app.baselib.Utils.GsonUtils;
import com.yzl.appres.pay.alipay.PayResult;
import com.yzl.appres.pay.alipay.SignUtils;
import com.yzl.appres.pay.listener.IPayBean;
import com.yzl.appres.pay.model.AlpayBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 支付宝支付
 * Created by ADongdpa on 16/3/22.
 */
public class ALPay extends IPayBean {

    public static final int SDK_PAY_FLAG = 1;

    private static final String SUCCESS = "9000";
    private static final String WAIT_SURE = "8000";

    private String hint = "需要配置PARTNER | RSA_PRIVATE| SELLER";
    private String paySuccess = "支付成功";
    private String payFail = "支付失败";
    private String paySure = "支付结果确认中";

    private Activity mActivity;
    private AlpayBean payModel;

    public ALPay(Activity activity, AlpayBean payModel) {
        mActivity = activity;
        this.payModel = payModel;
        alpay();
    }

    /**接收支付宝返回的支付结果*/
    @SuppressWarnings("unchecked")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what != SDK_PAY_FLAG) {
                return;
            }
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            switch (resultStatus) {
                case SUCCESS:
                    toastMessage(paySuccess);
                    if (null != resultListener) resultListener.onPaySuccess();
                    break;

                case WAIT_SURE:
                    toastMessage(payFail);
                    break;

                default:
                    toastMessage(paySure);
                    if (null != resultListener) resultListener.onPayFail();
                    break;
            }
        }
    };

    private void toastMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /** 判断密匙或公钥. **/
    private void alpay() {
        if (TextUtils.isEmpty(payModel.getPartner())
                || TextUtils.isEmpty(payModel.getPrivate_key())
                || TextUtils.isEmpty(payModel.getSeller_email())) {
            toastMessage(hint);
            return;
        }

        String orderInfo = getOrderInfo(payModel);
        String sign = sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /** 完整的符合支付宝参数规范的订单信息 **/
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(AlpayBean payModel) {
        Log.e("paymodel", "paymodel:" + GsonUtils.getJsonFromObject(payModel));

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + payModel.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + payModel.getSeller_email() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + payModel.getPay_sn() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + payModel.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + payModel.getGoods_name() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + payModel.getOrder_amount() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + payModel.getNotify_url() + "\"";

        /** 下面是固定值，无需修改 **/

        // 服务接口名称
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型
        orderInfo += "&payment_type=\"1\"";

        // 参数编码
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    private String sign(String content) {
        return SignUtils.sign(content, payModel.getPrivate_key());
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
