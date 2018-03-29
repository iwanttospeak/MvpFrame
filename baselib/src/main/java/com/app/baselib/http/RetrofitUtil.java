package com.app.baselib.http;

import com.app.baselib.Utils.MD5Util;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author by Wang on 2017/12/15.
 * 用来管理 Retrofit,统一封装Header
 */
public class RetrofitUtil {

    private static OkHttpClient okHttpClient;

    private static String requestBodyString;
    //在需要用户token的请求中将token设置到请求头中
    private static String userToken;

    /**
     * 统一封装Header
     */
    public static Request genericClient(Request request) throws IOException {
        String content_type = "application/json;charset=UTF-8";
        String timestamp = System.currentTimeMillis()/1000 +"";
        String nonce =  MD5Util.getRandomString(10);
        String params =  timestamp + nonce;
        RequestBody requestBody = request.body();
        if (requestBody !=null) {
            if (StringUtils.isSpace(requestBodyString)) {
                LogUtils.w("request_body", " please create requestBody content json string");
            } else {
                params = timestamp + nonce + requestBodyString;
            }
        }
        requestBodyString = null;
        // Request customization: add request headers
        Request.Builder requestBuilder = request.newBuilder()
                .addHeader("Content-Type",content_type)
                .addHeader("timestamp", timestamp)
                .addHeader("nonce", nonce)
                .addHeader("sign",getSignHeader(params));
        if (!StringUtils.isSpace(userToken)){
            requestBuilder.addHeader("token",userToken);
        }
        return requestBuilder.build();
    }

    public static String getSignHeader(String params){
        return  MD5Util.MD532(params);
    }

    public static void setRequestBodyString(String requestString){
        requestBodyString = requestString;
    }

    public static void setToken(String token){
        userToken = token;
    }
}
