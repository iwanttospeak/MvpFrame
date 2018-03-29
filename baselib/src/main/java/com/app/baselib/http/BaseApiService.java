package com.app.baselib.http;

import com.app.baselib.bean.QNTokenBean;
import com.app.baselib.http.bean.WrapDataBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 进行特定网络请求的ApiService
 */
public interface BaseApiService {

    /* ------------------------------------Api接口-----------------------------------*/
    /**
     * 发送验证码.
     */
    @FormUrlEncoded
    @POST("app/userAction/sendCode")
    Observable<WrapDataBean<String>> sendCode(@FieldMap Map<String, String> params);
    /**
     * 验证绑定邮箱.
     */
    @FormUrlEncoded
    @POST("userAction/bindSendEmail")
    Observable<WrapDataBean<String>> sendEmailCode(@Field("email") String email);

    /**
     * 解绑邮箱验证码.
     */
    @FormUrlEncoded
    @POST("userAction/unBindSendEmail")
    Observable<WrapDataBean<String>> sendUnBoundEmailCode(@Field("email") String email);

    /**
     * 获取七牛token
     */
    @FormUrlEncoded
    @POST("upload/getUploadToken")
    Observable<WrapDataBean<QNTokenBean>> getUploadToken(@FieldMap Map<String, String> params);




}
