package com.yzl.appres.router;

/**
 * Created by wang
 */

public class PersonalRouter {

    private static final String PERSONAL_START = "/personal/";

    /**
     * 账户管理.
     */
    public static final String ACCOUNT_MANAGER_ACTIVITY = PERSONAL_START + "account_manager_activity";

    /**
     * 资金管理.
     */
    public static final String BALANCE_ACTIVITY = PERSONAL_START + "balance_activity";

    /**
     * 详情页面>
     */
    public static final String DETAIL_ACTIVITY = PERSONAL_START + "detail_activity";

    /**
     * 我的收藏.
     */
    public static final String MY_COLLECTION_ACTIVITY = PERSONAL_START + "my_collection_activity";

    /**
     * 我的足迹.
     */
    public static final String MY_FOOTPRINT_ACTIVITY = PERSONAL_START + "my_footprint_activity";

    /**
     * 地址管理.
     */
    public static final String ADDRESS_MANAGER_ACTIVITY = PERSONAL_START + "address_manager_activity";

    /**
     * 我想买.
     */
    public static final String WANT_BUY_ACTIVITY = PERSONAL_START + "want_buy_activity";

    /**
     * 添加地址.
     */
    public static final String ADD_ADDRESS_ACTIVITY = PERSONAL_START + "add_address_activity";

    /**
     * 优惠券.
     */
    public static final String COUPON_ACTIVITY = PERSONAL_START + "coupon_activity";
}
