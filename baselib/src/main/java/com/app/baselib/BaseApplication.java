package com.app.baselib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取全局Context的方法必须为:BaseUtils.getContext()，不允许其他写法；
 * BaseApplication主要用来管理全局Activity;
 */
public class BaseApplication extends MultiDexApplication {

    private Stack<Activity> mActivitys = new Stack<>();

    private static Context mApplicationContext;

    /**
     * 该context很多xml都引用到了,请勿更改application名称和该方法名称.
     */
    public static final Context getContext() {
        return mApplicationContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;

        //第三方开源库Calligraphy初始化字体库
//        CalligraphyConfig.initDefault(new CalligraphyConfig.QrScanConfigurationBuilder()
//                .setDefaultFontPath("fonts/square_black_simple.ttf")//字体文件位置
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        //第三方开源库Utils初始化
        Utils.init(this);

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);

        //初始化极光推送
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签

        //初始化环信
        initHyphenate();

       registerActivity();
    }
    /**
     * 初始化环信sdk，并做一些注册监听的操作，这里把其他的处理都去掉了只写了小米推送
     */
    private void initHyphenate() {
    }

    private void registerActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity != null) mActivitys.add(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
            }
            @Override
            public void onActivityPaused(Activity activity) {
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mActivitys.contains(activity)) mActivitys.remove(activity);
            }
        });
    }

    public void destroyAllActivity() {
        for (Activity activity : mActivitys) {
            if (!activity.getClass().getSimpleName().equals("HomeActivity"))
                activity.finish();
        }
    }
}
