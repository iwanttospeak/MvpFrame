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

import com.app.baselib.R;
import com.app.baselib.Utils.SoftInputUtils;
import com.blankj.utilcode.util.StringUtils;

/**
 * 搜索功能layout
 * @author by Wang on 2017/8/2.
 */
public class SearchLayout extends LinearLayout {

    public void setSearchByInput(boolean searchByInput) {
        this.searchByInput = searchByInput;
    }

    /**
     * 是否一边输入一边搜索
     */
    private boolean searchByInput;

    public EditText getEditText() {
        return editText;
    }

    //编辑框
    private EditText editText;
    //搜索按钮
    private ImageView imageView;

    private LinearLayout rootLayout;

    public void setRootBackgroundColor(int color){
        rootLayout.setBackgroundColor(color);
    }
    public void setEditHint(String editHint) {
        if (!StringUtils.isSpace(editHint)){
            editText.setHint(editHint);
        }
    }
    public void setEditContent(String content){
        if (!StringUtils.isSpace(content)){
            editText.setText(content);
        }
    }
    public void setRootDrawable(Drawable rootDrawable) {
        this.rootDrawable = rootDrawable;
    }
    public void showImageView(){
        imageView.setVisibility(VISIBLE);
    }
    public void hideImageView(){
        imageView.setVisibility(GONE);
    }

    //根布局的background
    private Drawable rootDrawable;

    private OnSearchListener listener;

    public SearchLayout(Context context) {
        super(context);
        initView();
    }
    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.base_layout_search,this,true);
        editText = (EditText) findViewById(R.id.et_search_key);
        imageView = (ImageView) findViewById(R.id.iv_search_button);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootDrawable !=null){
            rootLayout.setBackground(rootDrawable);
        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onSearch(StringUtils.isSpace(editText.getText().toString()) ? "" : editText.getText().toString(),false);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (searchByInput){
                    //处理事件
                    if (listener !=null) {
                        listener.onSearch(StringUtils.isSpace(editText.getText().toString()) ? "" : editText.getText().toString(),true);
                    }
                }
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    //处理事件
                    if (listener !=null) {
                        listener.onSearch(StringUtils.isSpace(editText.getText().toString()) ? "" : editText.getText().toString(),false);
                    }
                    return true;
                }
                return false;
            }
        });

    }
    //是否editText失去焦点隐藏软件盘
    public void setClickOurHindSoft(boolean clickOurHindSoft, Activity activity) {
        if (clickOurHindSoft) {
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {//失去焦点
                        SoftInputUtils.hideSoftInput(activity);
                    }
                }
            });
        }
    }
    public void setOnFocusChangeListener(OnFocusChangeListener listener){
//        editText.clearFocus();
        editText.setOnFocusChangeListener(listener);
    }

    public interface  OnSearchListener{
        void onSearch(String key, boolean searchByInput);
    }

    public void setOnSearchListener(OnSearchListener listener){
        this.listener = listener;
    }

}
