package com.app.modulelogin.constant;

/**
 * @author by admin on 2017/7/7.
 */

public class LoginConstant {

    /**发送验证码注册.*/
    public static final int CODE_TYPE_REGISTER = 0;
    /**发送验证码忘记密码*/
    public static final int CODE_TYPE_FIND_PASSOWRD = 1;
    /**发送验证码重置密码.*/
    public static final int CODE_TYPE_CHANGE_PASSWORD = 4;

    public static final int CODE_TYPE_PERFECT = 3;

    public static final int CODE_TYPE_BOUND_EMAIL = 5;


    //用来让自动填写验证码的框架识别你的短信
    public static final String APP_NAME = "电镀家";
    //用来让自动填写验证码的框架识别你的短信
    public static final int SMS_START_NUMBER= 1069144;

    /**
     * 绑定手机.
     */
    public static final int CODE_TYPE_BOUND_PHONE = 8;

    /**
     * 更换手机号.
     */
    public static final int CODE_TYPE_CHANGE_PHONE = 9;
}
