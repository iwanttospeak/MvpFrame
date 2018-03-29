package com.yzl.moduleforum.ui.forumList;

import android.support.v4.app.FragmentTransaction;

import com.app.baselib.mvp.BaseActivity;
import com.yzl.moduleforum.R;


public class ForumListActivity extends BaseActivity  {

    private FragmentTransaction fragmentTransaction;


    @Override
    public int getLayoutId() {
        return R.layout.forum_activity_forum_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void init() {
        initFragment();
    }
    private void initFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment_wrap, ForumListFragment.getInstance());
        fragmentTransaction.commit();
    }

}
