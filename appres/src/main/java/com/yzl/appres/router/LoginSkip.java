package com.yzl.appres.router;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * 登录模块的页面跳转映射
 * Created by shen on 2017/5/10.
 */

public class LoginSkip {

    private static final String LOGIN_START = "/login/";

    public static final String LOGIN_TEST = LOGIN_START + "test";


    public static final String LOGIN_LOGIN = LOGIN_START + "login";
    //Boolean类型是否回退到首页
    public static final String LOGIN_LOGIN_KEY = "is_to_home";
    //Boolean类型是否从首页过来首页
    public static final String LOGIN_IS_COME_FROM_HOME = "is_come_from_home";

    public static final String LOGIN_REGISTER = LOGIN_START +"register";
    //注册接收验证码
    public static final String LOGIN_REGISTER_CODE = LOGIN_START +"register_code";


    public static final String LOGIN_PERFECT = LOGIN_START + "perfect";

    public static final String LOGIN_REGISTER_RESULT = LOGIN_START +"register_result";

    /**
     * 更换手机提示页.
     */
    public static final String CHANGE_PHONE_HINT = LOGIN_START +"change_phone_hint";

    /**
     * 更换手机.
     */
    public static final String CHANGE_PHONE= LOGIN_START +"change_phone";

    /**
     * 忘记密码1
     */
    public static final String FORGET_PASSOWRD = LOGIN_START + "forget_password";

    /**
     * 忘记密码2
     */
    public static final String FORGET_RESET_PASSOWRD = LOGIN_START +"reset_password";

    /**
     * 修改密码
     */
    public static final String CHANGE_PASSWARD= LOGIN_START +"change_password";
    /**
     * 第三方登录完善信息.
     */
    public static final String THIRD_PART_PERFECT = LOGIN_START +"third_part_perfect";

}
