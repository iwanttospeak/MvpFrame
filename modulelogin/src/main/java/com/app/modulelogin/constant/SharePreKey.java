package com.app.modulelogin.constant;

import com.blankj.utilcode.util.SPUtils;
import com.yzl.appres.constant.SpUtilCommon;

/**
 * 此sharePreference集成中存储login的数据
 * Created by shen on 2017/5/11.
 */

public class SharePreKey   {

    //app公用数据存储位置
    private SPUtils commonSPUtils= SpUtilCommon.spUtils;

    public static SPUtils loginSpUtils = SharePreKey.SingleTask.sputils;

    private static final class SingleTask {
        public static final SPUtils sputils = SPUtils.getInstance("login");
    }
    public static final String PERSONAL_INFO = "personal_info";

    public static final String PERSON_INFO = "person_info";

}
