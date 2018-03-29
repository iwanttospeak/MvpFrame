package com.app.baselib.constant;

import com.blankj.utilcode.util.SPUtils;

/**
 * 管理底层框架所需要的SharePrefer
 * @author by admin on 2017/7/7.
 */

public class BaseSpUtils {


    public static SPUtils spUtils = SingleTask.sputil;

    private static final class SingleTask {
        public static final SPUtils sputil =  SPUtils.getInstance("base");
    }
    //当前用户Id
    public static final String CURRENT_USER_ID = "current_user_id";

    //用户token
    public static final String TOKEN = "token";

}
