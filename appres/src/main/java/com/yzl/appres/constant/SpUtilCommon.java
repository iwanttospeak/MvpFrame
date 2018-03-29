package com.yzl.appres.constant;

import com.blankj.utilcode.util.SPUtils;

/**
 * 此sharePreference中存储应用共享的数据，
 * 应该被各个module的SharePreKey继承
 */

public class SpUtilCommon {

    public static SPUtils spUtils = SingleTask.sputils;

    private static final class SingleTask {
        public static final SPUtils sputils =  SPUtils.getInstance("ciger");
    }

    public static final String WELCOME_VERSION_NAME = "welcome_version_name";

    //是否初始化APP
    public static final String IS_INIT_APP = "is_init_app";




    public static final String AGREEMENT = "agreement";

    public static final String LOGIN_STYLE = "login_style";

    public static final String PERSONAL_INFO = "personal_info";


    public static final String LOGIN_BEAN = "login_bean";

    public static final String HX_ID = "hx_id";

}
