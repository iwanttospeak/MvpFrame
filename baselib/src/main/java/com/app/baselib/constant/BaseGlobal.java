package com.app.baselib.constant;

import static com.app.baselib.constant.BaseSpUtils.CURRENT_USER_ID;
import static com.app.baselib.constant.BaseSpUtils.TOKEN;
import static com.app.baselib.constant.BaseSpUtils.spUtils;

/**
 * 保存基础框架所需要的全局数据,不保存任何实体类信息
 * @author by admin on 2017/7/7.
 */

public class BaseGlobal {


    /**
     * 清除所有资源.数据退出登录或者登录失效
     */
    public static void clearAll(){

    }

    /**
     * 得到当前用户id
     */
    public static String getCurrentUserId() {
        return spUtils.getString(CURRENT_USER_ID);
    }
    /**
     * 保存当前用户id，为了保存对应每个人的极光推送的消息提醒的数据
     */
    public static void saveCurrentUserId(String user_id) {
        spUtils.put(CURRENT_USER_ID,user_id);
    }

    /**
     * 保存token.
     */
    public static void saveToken(String token) {
        spUtils.put(TOKEN, token);
    }

    /**
     * 获取token.
     */
    public static String getToken() {
        return spUtils.getString(TOKEN);
    }
}
