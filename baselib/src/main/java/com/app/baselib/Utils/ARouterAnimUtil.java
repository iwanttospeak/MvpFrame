package com.app.baselib.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by shen on 2017/5/11.
 */

public class ARouterAnimUtil {
    //ARouter也支持过场动画
    public static void goActivity(String activityName, Activity activity) {
        ARouter.getInstance().build(activityName).withOptionsCompat(ActivityOptionsCompat.makeSceneTransitionAnimation(activity)).navigation(activity);
    }

    public static void goActivity(String activityName, ActivityOptionsCompat optionsCompat, Activity activity) {
        ARouter.getInstance().build(activityName).withOptionsCompat(optionsCompat).navigation(activity);
    }


    public static void goActivity(String activityName, int flag) {
        ARouter.getInstance().build(activityName).withFlags(flag).navigation();
    }

    public static void goActivity(String activityName, Bundle bundle, Activity activity) {
        ARouter.getInstance().build(activityName).with(bundle).withOptionsCompat(ActivityOptionsCompat.makeSceneTransitionAnimation(activity)).navigation(activity);
    }

    public static void goActivity(String activityName, Bundle bundle, ActivityOptionsCompat optionsCompat, Activity activity) {
        ARouter.getInstance().build(activityName).with(bundle).withOptionsCompat(optionsCompat).navigation(activity);
    }
}
