package com.app.baselib.Utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by 沈小建 on 2017-12-18.
 */

public class ReminderUtils {

    public final static void showMessage(String str) {
        ToastUtils.setBgColor(Color.parseColor("#ffa4a3"));
        ToastUtils.showShort(str);
    }

    public final static void showMessage(int strId) {
        ToastUtils.setBgColor(Color.parseColor("#ffa4a3"));
        ToastUtils.showShort(strId);
    }

    public final static void showWarnMessage(String str) {
        ToastUtils.setBgColor(Color.YELLOW);
        ToastUtils.showShort(str);
    }

    public final static void showWarnMessage(int strId) {
        ToastUtils.setBgColor(Color.YELLOW);
        ToastUtils.showShort(strId);
    }


    public final static void showErrorMessage(String str) {
        ToastUtils.setBgColor(Color.RED);
        ToastUtils.showShort(str);
    }

    public final static void showErrorMessage(int strId) {
        ToastUtils.setBgColor(Color.RED);
        ToastUtils.showShort(strId);
    }

    public final static void showToast(Context context, int strId) {
        Toast.makeText(context, context.getString(strId), Toast.LENGTH_SHORT).show();
    }
}
