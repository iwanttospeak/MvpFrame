package com.app.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import com.app.baselib.R;
import com.app.baselib.Utils.ReminderUtils;
import com.app.baselib.http.Api;
import com.app.baselib.http.BaseApiService;
import com.app.baselib.http.NetRequest;
import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;

import org.reactivestreams.Subscription;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * 发送验证码
 * Created by shen on 2017/5/10.
 */

public class SendCodeTV extends AppCompatTextView {

    /**
     * 手机号.
     */
    private String mPhoneStr;

    /**
     * 手机号对应的editText.
     */
    private EditText mEtPhone;

    /**
     * 短信类型.
     * 0-注册，1-重置密码，4-修改密码
     */
    private int type;

    /**
     * 字体大小.
     */
    private  float tv_size;


    /**
     * 普通状态下的字体颜色.
     */
    private  int normal_tv_color;

    /**
     * 等待状态下的字体颜色.
     */
    private  int wait_tv_color;

    private  String wait_text;

    private  int wait_backgraound;

    private int normal_background;
    /**
     * 等待时间.
     */
    private static  int WAIT_TIME;
    private static final int SECOND = 1;

    private Disposable mCountDownSubscrbe;

    /**
     * 网络对象.
     */
    private NetRequest mNetRequest;
    /**
     * 是否邮箱.
     */
    private boolean isEmail;

    /**
     * 是否解绑邮箱.
     */
    private boolean isUnbound;

    public void setUnbound(boolean unbound) {
        isUnbound = unbound;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }

    public void setPhoneStr(String mPhoneStr) {
        this.mPhoneStr = mPhoneStr;
    }

    public void setEtPhone(EditText mEtPhone) {
        this.mEtPhone = mEtPhone;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SendCodeTV(Context context) {
        super(context);
    }

    public SendCodeTV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

        setNormalState();
    }

    private void initAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SendCodeTV);
        initPointView(typedArray);
        typedArray.recycle();
    }

    private void initPointView(TypedArray typedArray) {
        normal_background = typedArray.getResourceId(R.styleable.SendCodeTV_send_code_normal_background, R.drawable.code_bg_white);
        wait_backgraound = typedArray.getResourceId(R.styleable.SendCodeTV_send_code_wait_background, R.drawable.code_bg_white);

        wait_text = typedArray.getString(R.styleable.SendCodeTV_send_code_normal_text);
        tv_size = typedArray.getDimension(R.styleable.SendCodeTV_send_code_text_size,13);
        normal_tv_color = typedArray.getColor(R.styleable.SendCodeTV_send_code_normal_text_color, ContextCompat.getColor(getContext(), R.color.white));
        WAIT_TIME = typedArray.getInt(R.styleable.SendCodeTV_send_code_wait_time,60);

    }

    /**
     * 普通状态可点击.
     */
    private void setNormalState() {
        setClickable(true);
        setText(StringUtils.isSpace(wait_text)? getContext().getString(R.string.code_send_code) : wait_text);
        setBackgroundResource(normal_background);
        setTextSize(tv_size);
        setTextColor(normal_tv_color);
        setOnClickListener(v -> onClickListener());
    }
    /**
     * 已发送等待60秒状态.
     */
    private void setWaitState() {
        setBackgroundResource(wait_backgraound);
    }
    /**
     * 点击事件.
     */
    private void onClickListener() {
        String phone;

        if (mEtPhone != null) {
            phone = mEtPhone.getText().toString();
        } else {
            phone = mPhoneStr;
        }

        if (checkPhone(phone)) {
            if (isEmail) {
//                requestSendEmailCode(phone);
            } else if (isUnbound) {
//                requestSendunBoundEmailCode(phone);
            } else {
                requestSendCode(phone);
            }
        }
    }

    /**
     * 检查手机号.
     */
    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), getStrById(R.string.code_empty_phone_hint), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!RegexUtils.isMobileExact(phone) && !isEmail && !isUnbound) {
            Toast.makeText(getContext(), getStrById(R.string.code_correct_phone_hint), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!RegexUtils.isEmail(phone) && (isEmail || isUnbound)) {
            Toast.makeText(getContext(), getStrById(R.string.code_correct_phone_hint), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * 发送验证码.
     */
    private void requestSendCode(String phone) {
        setClickable(false);
        Map<String, String> params = new ArrayMap<>();
        params.put("phone", phone);
        params.put("type", type + "");


        if (mNetRequest == null) {
            mNetRequest = new NetRequest(getContext());
        }
        mNetRequest.requestWithDialog(Api.getInstance().getApiService(BaseApiService.class).sendCode(params), new CallBack<String>() {
            @Override
            public void onResponse(String data) {
                requestSendCodeSuccess();
            }
        }, new ErrorBack() {
            @Override
            public void onFailure(Object errorData) {
                ReminderUtils.showErrorMessage(R.string.code_send_code_fail);
                setClickable(true);
            }
        }, true);
    }

    private void requestSendCodeSuccess() {
        Toast.makeText(getContext(), getStrById(R.string.code_send_code_success), Toast.LENGTH_SHORT).show();
        setWaitState();

        //开始倒计时.
        mCountDownSubscrbe = Observable.interval(0, SECOND, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong == WAIT_TIME) {
                        mCountDownSubscrbe.dispose();
                        setNormalState();
                        return;
                    }

                    setText(getContext().getString(R.string.code_wait, WAIT_TIME - aLong + ""));
                });
    }

    private String getStrById(int strId) {
        return getContext().getString(strId);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCountDownSubscrbe != null) mCountDownSubscrbe.dispose();
    }
}
