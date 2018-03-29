package com.yzl.appres.constant;

/**
 * @author by Wang
 */

public class LibConstant {

    public static final int CODE_TYPE_REGISTER = 0;

    public static final int CODE_TYPE_FIND_PASSOWRD = 1;

    public static final int CODE_TYPE_PERFECT = 3;

    public static final int CODE_TYPE_BOUND_EMAIL = 5;

    public static final int CODE_TYPE_CHANGE_PASSWORD = 6;

    /**
     * 支付方式
     **/
    public static final int BALANCE = 1;  //钱包
    public static final int ALIPAY = 2; //支付宝
    public static final int WECHAT = 3; //微信
    /**
     * qq.
     */
    public static final String QQ_APP_ID = "1105462469";

    /**
     * 支付类型(支付订单,充值).
     */
    public static final String ORDER_TYPE_BUY = "buy";

    /**
     * 微信的key.
     */
    public static final String WX_KEY = "wxe550da26a773f9e3";
    /** 微信支付id **/
    public static final String WX_APP_ID = "wxd1514b1bb4893c26";

    /**
     * 微博的key.
     */
    public static final String WB_KEY = "3258306056";
    public static final String WB_REDIRECT_URL = "http://www.sina.com";// 应用的回调页
    public static final String WB_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    /**
     * eventBus支付成功事件
     **/
    public static final String PAY_SUCCESS = "pay_success";
}
