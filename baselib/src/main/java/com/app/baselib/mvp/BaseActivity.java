package com.app.baselib.mvp;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.baselib.R;
import com.app.baselib.Utils.GenericityUtils;
import com.app.baselib.Utils.StateBarDarkUtil;
import com.app.baselib.Utils.StringUtil;
import com.app.baselib.constant.BaseConstants;
import com.app.baselib.widget.LoadingDialog;
import com.app.baselib.widget.state.StateUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.luck.picture.lib.rxbus2.Subscribe;
import com.luck.picture.lib.rxbus2.ThreadMode;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


/**
 *activity基类，默认实现右划返回上一页面
 * Created by wang on 2017/7/4.
 */
public abstract class BaseActivity<T extends BasePresent> extends AppCompatActivity {
    public T mPresent;

    protected LoadingDialog loadingDialog;
    /**
     * 是否有toolbar.
     */
    private boolean isHaveToolbar;
    /**
     * 多状态.
     */
    protected StateUtils mModeStateView;

    /**
     * 是否显示多状态.
     */
    private boolean isShowStateView = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        //loadingDailog
        loadingDialog = new LoadingDialog(this);
        ButterKnife.bind(this);
        if (useEventBus())//如果要使用 Eventbus 请将此方法返回 true
            EventBus.getDefault().register(this);
        initStateTextColorBlack(false);
        initToolbar();
        initStateView();
        initStateTransparent(true);
        initPresent();
        init();
        if (mPresent != null) mPresent.start();
    }


    /**
     * 初始化泛型present.
     */
    private void initPresent() {
        GenericityUtils utils = new GenericityUtils();
        Class clazz = utils.getGenericClass(this);
        if (clazz == null) return;

        utils.checkGenericity(clazz, getClass().getName());
        mPresent = (T) utils.instantiationClass(clazz);
        mPresent.attach(this);
        mPresent.init(mModeStateView);
    }


    /**
     * 初始化toolbar.
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        isHaveToolbar = toolbar != null;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.base_arrows_black_left);
            toolbar.setNavigationOnClickListener(view -> {
                backHandle();
            });
        }
    }
    /**
     * 设置是否显示多状态.
     *
     * @param showStateView true显示,false不显示,默认显示
     */
    public void setShowStateView(boolean showStateView) {
        isShowStateView = showStateView;
    }

    /**
     * 初始化多状态列表.
     */
    protected void initStateView() {
        if (mModeStateView == null && isShowStateView) {
            mModeStateView = new StateUtils(this, isHaveToolbar);
        }
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     */
    public boolean useEventBus() {
        return true;
    }
    public void backHandle() {
        KeyboardUtils.hideSoftInput(this);
        finish();
    }
    /**
     * 设置系统状态栏透明
     */
    public void initStateTransparent(boolean isTransparent) {
        if (isTransparent){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    /**
     * 设置状态栏字体颜色为黑色.
     */
    public void initStateTextColorBlack(boolean isBlack ) {
        if (isBlack){
            StateBarDarkUtil.setBlack(this, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    /**
     * 设置状态栏颜色
     */
    public void setNowStatusColor(@ColorRes int colorId) {
        BarUtils.setStatusBarColor(this,ContextCompat.getColor(this, colorId));
    }
    public void setToolTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }
    public void setToolTitle(int titleRes) {
        TextView textView = (TextView) findViewById(R.id.tv_title);
        if (textView != null) textView.setText(titleRes);
    }
    public void setRightTitle(String content) {
        TextView textView = (TextView) findViewById(R.id.tv_right);
        if (textView != null) textView.setText(content);
    }
    public TextView getRightTextView() {
       return (TextView) findViewById(R.id.tv_right);
    }
    @Override
    public void setRequestedOrientation(int requestedOrientation){
        return;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresent != null) mPresent.detach();
        if (useEventBus())
            EventBus.getDefault().unregister(this);//解除注册 Eventbus
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finish(String msg){
        if (msg.equals(BaseConstants.FINISH_ALL)) finish();
    }

    /** 显示加载对话框 **/
    public void showProcessDialog(){
        if(loadingDialog == null ){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    /** 取消加载对话框 **/
    public void dismissProcessDialog(){
        if(loadingDialog == null ){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.dismiss();
    }

    /*------------------------------------------需要实现的方法------------------------------------------------*/
    /**
     * 对应的布局.
     */
    public int getLayoutId(){
        return -1;
    }
    /**
     * 初始化布局开始执行的方法.
     */
    public void init() {
    }

}
