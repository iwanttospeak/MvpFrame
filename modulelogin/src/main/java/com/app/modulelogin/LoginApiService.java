package com.app.modulelogin;


import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.bean.WrapDataNoContent;
import com.app.modulelogin.bean.QqLoginBean;
import com.app.modulelogin.bean.WeiXinLoginBean;
import com.yzl.appres.bean.LoginBean;
import com.yzl.appres.bean.PersonalInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author by admin on 2017/7/6.
 */

public interface LoginApiService {

    /**
     * 登陆.
     */
    @FormUrlEncoded
    @POST("customer/login")
    Observable<WrapDataBean<LoginBean>> login(@FieldMap Map<String, String> params);

    /**
     * 微信登陆.
     */
    @FormUrlEncoded
    @POST("userAction/OauthLogin")
    Observable<WrapDataBean<WeiXinLoginBean>> weiXinLogin(@FieldMap Map<String, String> params);

    /**
     * qq登陆.
     */
    @FormUrlEncoded
    @POST("userAction/OauthLogin")
    Observable<WrapDataBean<QqLoginBean>> qqLogin(@FieldMap Map<String, String> params);


    /**
     * 注册.
     */
    @FormUrlEncoded
    @POST("customer/register")
    Observable<WrapDataBean<LoginBean>> register(@FieldMap Map<String, String> params);
    /**
     * 注册验证码验证.
     */
    @FormUrlEncoded
    @POST("customer/registerVerify")
    Observable<WrapDataNoContent<LoginBean>> registerVerify(@FieldMap Map<String, String> params);

    /**
     * 完善信息.
     */
    @FormUrlEncoded
    @POST("app/userAction/perInfomation")
    Observable<WrapDataBean<PersonalInfoBean>> perfectInfo(@FieldMap Map<String, String> params);

    /**
     * 发送验证码.
     */
    @FormUrlEncoded
    @POST("userAction/sendCode")
    Observable<WrapDataBean<String>> sendCode(@FieldMap Map<String, String> params);
    /**
     * 更换绑定手机.
     */
    @FormUrlEncoded
    @POST("app/userAction/changeBindPhone")
    Observable<WrapDataBean<String>> changePhone(@FieldMap Map<String, String> params);
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
     * 绑定邮箱.
     */
    @FormUrlEncoded
    @POST("userAction/bindEmail")
    Observable<WrapDataBean<String>> boundEmail(@FieldMap Map<String, String> params);

    /**
     * 解绑邮箱.
     */
    @FormUrlEncoded
    @POST("userAction/unbindEmail")
    Observable<WrapDataBean<String>> unBoundEmail(@FieldMap Map<String, String> params);

    /**
     * 重置密码.
     */
    @FormUrlEncoded
    @POST("app/userAction/resetPasswd")
    Observable<WrapDataBean<String>> resetPassword(@FieldMap Map<String, String> params);
    /**
     * 忘记密码步骤1
     */
    @FormUrlEncoded
    @POST("customer/forgetPwdStep1")
    Observable<WrapDataNoContent> forgetPwdStep1(@FieldMap Map<String, String> params);
    /**
     * 忘记密码步骤2,重置密码.
     */
    @FormUrlEncoded
    @POST("customer/forgetPwdStep2")
    Observable<WrapDataNoContent> forgetPwdStep2(@FieldMap Map<String, String> params);


    /**
     * 修改密码.
     */
    @FormUrlEncoded
    @POST("app/userAction/EditPwd")
    Observable<WrapDataBean<String>> editPassword(@FieldMap Map<String,String> params);

}
