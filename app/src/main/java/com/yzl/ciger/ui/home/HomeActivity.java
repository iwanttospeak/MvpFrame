package com.yzl.ciger.ui.home;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.baselib.bean.MenuItemBean;
import com.app.baselib.constant.BaseGlobal;
import com.app.baselib.mvp.BaseActivity;
import com.app.baselib.widget.FragmentAdapter;
import com.blankj.utilcode.util.ToastUtils;
import com.laojiang.imagepickers.utils.PopupWindowUtil;
import com.yzl.appres.event.BaseModuleEvents;
import com.yzl.appres.router.AppSkip;
import com.yzl.ciger.R;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



@Route(path = AppSkip.HOME_ACTIVITY)
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    TabLayout mTab;
    ViewPager mViewPage;

    private Toolbar toolbar;
    ImageView rightImg;
    private List<TextView> tabViewList = new ArrayList<>();
    //未读小红点提示
    private List<ImageView> noReadHintList = new ArrayList<>();
    /**对应fragment，和tab资源列表.*/
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<Integer> mResIdList = new ArrayList<>();
    //首页Tab显示
    private static final String[] homeTabNames = {"消息","通讯录","群聊","人脉圈","我的"};
    /** 点击时间记录.*/
    private long preTime;
    /** 两次点击后退间隔.**/
    private static final int TWO_SECOND = 2000;


    @Override
    public int getLayoutId() {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void adaptTheme(final boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
    @Override
    public void init() {
        mViewPage = (ViewPager) findViewById(R.id.home_page);
        mTab = (TabLayout) findViewById(R.id.home_tab);
        rightImg = findViewById(R.id.toolbar_img_right);
        toolbar  =  findViewById(R.id.toolbar);
        initVpAdapter();
        initBar(0);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                initBar(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化.
     */
    private void initVpAdapter() {
        initViewPagerData();
        FragmentAdapter homeAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        homeAdapter.setTitles(Arrays.asList(homeTabNames));
        mViewPage.setAdapter(homeAdapter);
        mTab.setupWithViewPager(mViewPage);
        initTabData();
    }

    private void initTabData() {
        for (int i = 0; i < mFragmentList.size(); i++) {
            FrameLayout tabRoot = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.app_tab_home,null,false);
            TextView tabView = (TextView) tabRoot.findViewById(R.id.tv_tab);
            ImageView noReadHint  = (ImageView) tabRoot.findViewById(R.id.iv_tab_no_read_hint);
            noReadHintList.add(noReadHint);
            tabView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, mResIdList.get(i), 0, 0);
            tabView.setText(homeTabNames[i]);

            mTab.getTabAt(i).setCustomView(tabRoot);
            tabViewList.add(tabView);
        }
    }


    private void initViewPagerData() {
//        mFragmentList.add(new UserCenterFragment());
        mResIdList.add(R.drawable.app_tab_message);
        mResIdList.add(R.drawable.app_tab_contact);
        mResIdList.add(R.drawable.app_tab_out_team);
        mResIdList.add(R.drawable.app_tab_order_dynamic);
        mResIdList.add(R.drawable.app_tab_mine);
    }

    /**
     * 初始化标题栏,有些需要判断是否登录
     **/
    private void initBar(int position) {
        hideNavigationIcon();
        rightImg.setVisibility(View.VISIBLE);
        for (TextView textView :tabViewList){
            textView.setTextColor(ContextCompat.getColor(this,R.color.base_tv_black));
        }
        tabViewList.get(position).setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        addRightActionBar(position);
    }

    /** 隐藏返回 **/
    protected void hideNavigationIcon(){
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 截获按钮，两次按钮退出.
     **/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();
            if ((currentTime - preTime) > TWO_SECOND) {
                ToastUtils.showShort("再按一次退出程序");
                preTime = currentTime;
                return true;
            }
//            AppLibUtils.obtainAppComponent(this).appManager().appExit();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // 添加action bar的右侧按钮及响应事件
    private void addRightActionBar(int position) {

        if (position == 0){
        }else if (position == 1){
        }else if (position == 2){
        }else if (position == 3){
//            rightImg.setImageResource(R.drawable.lib_nav_release);
            rightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else if (position == 4){
            rightImg.setVisibility(View.GONE);
        }
    }


    @Override
    public void showLoading() {
        showProcessDialog();
    }

    @Override
    public void hideLoading() {
        dismissProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 退出登录,被踢出
     */
    private void userLoginOut() {
//        BaseGlobal.saveLogin(false,BaseGlobal.getCurrentUserId());
//        BaseGlobal.saveUserToken("",BaseGlobal.getCurrentUserId());
        BaseGlobal.saveCurrentUserId("");
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(BaseModuleEvents.getIntence().new DispatchKeyEvent(event));
            mTab.setVisibility(View.VISIBLE);
        }
        return super.dispatchKeyEvent(event);
    }
}
