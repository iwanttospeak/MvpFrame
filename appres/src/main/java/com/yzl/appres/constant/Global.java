package com.yzl.appres.constant;

import android.text.TextUtils;

import com.app.baselib.Utils.GsonUtils;
import com.app.baselib.constant.BaseGlobal;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzl.appres.bean.LoginBean;
import com.yzl.appres.bean.PersonalInfoBean;

import java.util.ArrayList;
import java.util.List;

import static com.yzl.appres.constant.SpUtilCommon.AGREEMENT;
import static com.yzl.appres.constant.SpUtilCommon.HX_ID;
import static com.yzl.appres.constant.SpUtilCommon.IS_INIT_APP;
import static com.yzl.appres.constant.SpUtilCommon.LOGIN_BEAN;
import static com.yzl.appres.constant.SpUtilCommon.LOGIN_STYLE;
import static com.yzl.appres.constant.SpUtilCommon.PERSONAL_INFO;
import static com.yzl.appres.constant.SpUtilCommon.spUtils;

/**
 * 和应用有关的全局变量应该被各个module的继承，为什么不用一个呢？
 * 为了各个Module的复用性更强，将来不管你是移植ModuleLib还是移植某一个Module
 * 都可以非常方便不需要修改很多东西
 * Created by shen on 2017/5/11.
 */

public class Global {

    /**
     * 是否初始化APP
     */
    public static void saveIsInitApp(boolean initApp) {
        spUtils.put(IS_INIT_APP,initApp);
    }

    public static boolean getIsinitApp() {
        return spUtils.getBoolean(IS_INIT_APP,true);
    }

    /**
     * 得到当前用户id
     */
    public static String getCurrentUserId() {
        return BaseGlobal.getCurrentUserId();
    }
    /**
     * 保存当前用户id,保存到基础数据里面
     */
    public static void saveCurrentUserId(String user_id) {
        BaseGlobal.saveCurrentUserId(user_id);
    }

    /**
     * 保存登录状态.
     */
    public static void saveLogin(boolean isLogin) {
        spUtils.put(LOGIN_STYLE, isLogin);
    }

    /**
     * 获取登录状态.
     */
    public static boolean isLogin() {
        return spUtils.getBoolean(LOGIN_STYLE);
    }


    /**
     * 保存token.
     */
    public static void saveToken(String token) {
        BaseGlobal.saveToken(token);
    }

    /**
     * 获取token.
     */
    public static String getToken() {
        return BaseGlobal.getToken();
    }
    /**
     * 保存环信Id
     */
    public static void saveHXId(String hxId) {
        spUtils.put(HX_ID, hxId);
    }
    /**
     * 获取环信Id
     */
    public static String getHXId() {
        return spUtils.getString(HX_ID);
    }

    /**
     * 保存个人信息.
     */
    public static void saveLoginBean(LoginBean loginBean) {
        spUtils.put(LOGIN_BEAN, GsonUtils.getJsonFromObject(loginBean));
    }
    /**
     * 获取个人信息.
     */
    public static LoginBean getLoginBean() {
        String info = spUtils.getString(LOGIN_BEAN);
        if (TextUtils.isEmpty(info)) return null;
        return GsonUtils.getObject(info,LoginBean.class);
    }

    /**
     * 保存个人信息.
     */
    public static void savePersonalInfo(PersonalInfoBean infoBean) {
        spUtils.put(PERSONAL_INFO, new Gson().toJson(infoBean));
    }

    /**
     * 获取个人信息.
     */
    public static PersonalInfoBean getPersonalInfo() {
        String info = spUtils.getString(PERSONAL_INFO);
        if (TextUtils.isEmpty(info)) return null;

        return new Gson().fromJson(info, new TypeToken<PersonalInfoBean>() {
        }.getType());
    }

    /**
     * 保存协议地址.
     */
    public static void saveAgreement(String agreement) {
        spUtils.put(AGREEMENT, agreement);
    }

    public static String getAgreement() {
        return spUtils.getString(AGREEMENT, "");
    }

}
