package com.app.baselib.Utils;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by shen on 2017/5/11.
 */

public class ARouterUtil {

    public static void goActivity(String activity) {
        ARouter.getInstance().build(activity).navigation();
    }

    public static void goActivity(String activity, int flag) {
        ARouter.getInstance().build(activity).withFlags(flag).navigation();
    }

    public static void goActivity(String activity, Bundle bundle) {
        ARouter.getInstance().build(activity).with(bundle).navigation();
    }
}
