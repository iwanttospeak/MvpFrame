package com.laojiang.imagepickers.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.utils.CommonUtils;
import com.laojiang.imagepickers.utils.StateBarDarkUtil;

/**
 * 基类Activity
 */
public abstract class NoduleBaseActivity extends AppCompatActivity{
    protected View mContentView;
    protected Handler mHandler;

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        if (mContentView == null)
            mContentView = getLayoutInflater().inflate(getContentViewResId(), null);
        setContentView(mContentView);

        mHandler = new Handler(getMainLooper());
        initTitleBar();
        initUI(mContentView);
        initData();
        initListener();
    }
    protected void beforeSetContentView(Bundle savedInstanceState) {
        //去掉ActionBar
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //改变状态栏颜色
        CommonUtils.changeStatusBarColor(this, getResources().getColor(R.color.imagepicker_text_white));
        initStateTextColorBlack(true);
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
    private void initTitleBar() {
        if (findViewById(R.id.rl_toolbar) != null) {
           setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
           getSupportActionBar().setDisplayShowTitleEnabled(false);

            toolbar = findViewById(R.id.toolbar);
            if (toolbar!= null) {
                toolbar.setNavigationIcon(R.drawable.image_direction_left_gray);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }
    protected void setToolBarTitle(String title){
        TextView tvTitle = findViewById(R.id.toolbar_title);
        if (tvTitle !=null){
            tvTitle.setText(title);
        }
    }
    protected void setToolBarTitle(int str){
        TextView tvTitle = findViewById(R.id.toolbar_title);
        if (tvTitle !=null){
            tvTitle.setText(getString(str));
        }
    }
    protected void setRightText(String text){
        TextView tvTitle = findViewById(R.id.toolbar_text_right);
        if (tvTitle !=null){
            tvTitle.setText(text);
        }
    }




    /**
     * 查找View
     */
    protected <T extends View> T findView(int resId) {
        if (mContentView != null)
            return (T) mContentView.findViewById(resId);
        else
            return null;
    }
    /**
     * 弹出Toast
     *
     * @param resId 文字提示的资源id
     */
    public void showShortToast(final int resId)
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(NoduleBaseActivity.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出Toast
     *
     * @param msg 文字提示的字符串
     */
    public void showShortToast(final String msg)
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(NoduleBaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected abstract int getContentViewResId();

    protected abstract void initUI(View contentView);

    protected abstract void initData();
    protected abstract void initListener();


}
