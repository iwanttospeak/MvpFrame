package com.yzl.moduleforum.ui.forumList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.baselib.mvp.BaseFragment;
import com.yzl.moduleforum.R;


public class ForumListFragment extends BaseFragment<ForumListPresenter> implements ForumListContract.View {


    public static ForumListFragment getInstance() {
        return new ForumListFragment();
    }
    @Override
    public void init() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forum_list;
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
