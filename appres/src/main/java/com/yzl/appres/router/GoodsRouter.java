package com.yzl.appres.router;

/**
 * Created by wang
 */

public class GoodsRouter {

    private static final String GOODS_START = "/goods/";

    /**
     * 搜索页面.
     */
    public static final String SEARCH_ACTIVITY = GOODS_START + "search_activity";

    /**
     * 商品详情页面.
     */
    public static final String GOODS_DETAIL_ACTIVITY = GOODS_START + "goods_detail";

    /**
     * 分类商品>
     */
    public static final String CLASSIFY_ACTIVITY = GOODS_START + "classify_detail";
}
