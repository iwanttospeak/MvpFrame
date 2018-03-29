package com.app.baselib.http;

import com.app.baselib.http.intercept.AddParamsIntercept;
import com.app.baselib.http.intercept.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 单例类
 * @author 沈小建 on 2017/3/2 0002.
 */

public class Api {

    public Retrofit mRetrofit;
    //应用网络请求URL前缀
    private static final String baseUrl = "http://cigar-php.zertone2.com/app";

    private static class SingleHolder {
        private static final Api INSTANCE = new Api();
    }

    public static Api getInstance() {
        return SingleHolder.INSTANCE;
    }

    private Api() {
        AddParamsIntercept paramsIntercept = new AddParamsIntercept();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();

        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //okHttp客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(paramsIntercept)//参数拦截器
                .addInterceptor(logInterceptor)//log拦截器
                .build();
        //GSON设置
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        //Retrofit配置
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))//使用 Gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 Rxjava
                .build();
    }
    /**
     *  因为Retrofit强制只能反射的APIServer类只能是接口，而且不能进行继承，
     * 所以这里不能设置K继承什么基础接口之类的
     */
    public <T> T getApiService(Class<T> serviceClass){
       return mRetrofit.create(serviceClass);
    }
}
