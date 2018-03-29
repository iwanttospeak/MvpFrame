package com.app.baselib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.baselib.R;
import com.app.baselib.Utils.StringUtil;
import com.blankj.utilcode.util.StringUtils;

import butterknife.ButterKnife;

/**
 * 只有一个title和确定取消按钮的Dialog
 * @author by Wang on 2017/7/18.
 */

public class OnlyTitleDialog extends Dialog{

    private TextView  tvTitle;
    private TextView  tvCancel ;
    private TextView  tvPositive;

    //提示
    private String title;
    //取消text
    private String cancelText;
    //确认text
    private String confirmText;
    private Context context;

    private OnlyTitleDialog.OnClickListener listener;

    public OnlyTitleDialog(@NonNull Context context) {
        this(context, R.style.base_dialog_style);
    }

    public OnlyTitleDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_only_title_dialog);
        ButterKnife.bind(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        getWindow().setGravity(Gravity.CENTER);
        init();
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.tv_title_hint);
        if (!StringUtils.isSpace(title)){
            tvTitle.setText(title);
        }
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (!StringUtils.isSpace(cancelText)) {
            tvCancel.setText(cancelText);
        }
        tvPositive = (TextView) findViewById(R.id.tv_positive);
        if (!StringUtils.isSpace(confirmText)) {
            tvPositive.setText(confirmText);
        }
        tvCancel.setOnClickListener(view -> onClickCancel());
        tvPositive.setOnClickListener(view -> onClickPositive());
    }
    public void setHintTitle(String title){
        this.title = title;
    }
    public void setCancelText(String cancelText){
        this.cancelText = cancelText;
    }
    public void setPositiveText(String positiveText){
        this.confirmText = positiveText;
    }
    private void onClickCancel(){
        dismiss();
        if (listener != null) {
            listener.onClickCancel();
        }
    }
    private void onClickPositive() {
        dismiss();
        if (listener != null) {
            listener.onClickPositive();
        }
    }
    public interface OnClickListener {
        void onClickCancel();

        void onClickPositive();
    }

    public void setOnClickListener(OnClickListener clickPriceListener) {
        listener = clickPriceListener;
    }

}
