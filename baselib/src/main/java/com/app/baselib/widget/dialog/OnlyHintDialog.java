package com.app.baselib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.baselib.R;
import com.blankj.utilcode.util.StringUtils;

/**
 * 仅仅当作提示使用
 *
 * @author by Wang on 2017/7/19.
 */

public class OnlyHintDialog extends Dialog {

    private TextView tvDialogHint;
    private  ImageView ivDialogHintIcon;

    private String hintText;
    private int hintColor;
    private Bitmap hintBitmap;

    public OnlyHintDialog(@NonNull Context context) {
        this(context, R.style.base_dialog_style);
    }

    public OnlyHintDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_only_hint_dialog);
        tvDialogHint = (TextView) findViewById(R.id.tv_dialog_hint);
        ivDialogHintIcon = (ImageView) findViewById(R.id.iv_dialog_hint_icon);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        init();
    }

    public void setHintText(String hintText){
        this.hintText = hintText;

    }
    public void setTextColor(int colorId){
        this.hintColor = colorId;
    }
    public void setHintImage(Bitmap bitmap){
        this.hintBitmap = bitmap;
    }
    private void init() {

        if (hintColor != 0){
            tvDialogHint.setTextColor(ContextCompat.getColor(getContext(),hintColor));
        }

        if (!StringUtils.isSpace(hintText)){
            tvDialogHint.setText(hintText);
        }
        if (hintBitmap != null){
            ivDialogHintIcon.setImageBitmap(hintBitmap);
        }

    }
}
