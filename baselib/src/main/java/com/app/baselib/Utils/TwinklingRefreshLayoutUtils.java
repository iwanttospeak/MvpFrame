package com.app.baselib.Utils;


import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * 下拉刷新上拉加载控件TwinklingRefreshLayout工具类
 * @author by Wang on 2017/8/20.
 */

public class TwinklingRefreshLayoutUtils {

    /**
     * 对TwinklingRefreshLayout进行初始化设置
     */
    public static void initTwinklingLayout(TwinklingRefreshLayout mLlState){
        mLlState.setFloatRefresh(false);//开启android悬浮涂层加载
        mLlState.setOverScrollBottomShow(false);
        mLlState.setOverScrollTopShow(false);
    }
}
