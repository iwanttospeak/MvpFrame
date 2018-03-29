package com.app.baselib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.baselib.R;
import com.app.baselib.Utils.SoftInputUtils;
import com.blankj.utilcode.util.StringUtils;

/**
 * 导航栏搜索title 没有搜索功能 仅仅提供跳转
 */
public class SearchTitleHint extends LinearLayout {

    private LinearLayout rootLayout;
    private TextView tvHint;
    private OnSearchListener listener;

    public SearchTitleHint(Context context) {
        super(context);
        initView();
    }
    public SearchTitleHint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchTitleHint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    public void setEditHint(String editHint) {
        if (!StringUtils.isSpace(editHint)){
            tvHint.setHint(editHint);
        }
    }
    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.base_layout_search_hint,this,true);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        tvHint = (TextView) findViewById(R.id.tv_search);
    }

    public interface  OnSearchListener{
        void onSearch(String key, boolean searchByInput);
    }

    public void setOnSearchListener(OnSearchListener listener){
        this.listener = listener;
    }

}
