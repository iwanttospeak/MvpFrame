package com.yzl.ciger.ui.search;

import com.app.baselib.mvp.BaseActivity;
import com.yzl.ciger.R;


public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {


    @Override
    public int getLayoutId() {
        return R.layout.activity_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void init() {

    }

    @Override
    public void showLoading() {
        showProcessDialog();
    }

    @Override
    public void hideLoading() {
        dismissProcessDialog();
    }


}
