package com.app.baselib.http.intercept;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加参数.
 * @author wang
 */
public class AddParamsIntercept implements Interceptor {

    /**
     * 请求方式.
     */
    private static final String GET = "GET";
    private static final String POST = "POST";

    /**
     * 用户token对应网络的key.
     */
    private static final String TOKEN_KEY = "token";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        //Todo token
//        String token = BaseGlobal.getToken();
        String token = "";

        Request request = null;

        String method = original.method();
        if (method.equals(GET)) {
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter(TOKEN_KEY, token)
                    .build();
            Request.Builder requestBuilder = original.newBuilder().url(url);
            request = requestBuilder.build();
        } else if (method.equals(POST)) {
            if (original.body() instanceof FormBody) {
                FormBody.Builder newFormBody = new FormBody.Builder();
                FormBody oidFormBody = (FormBody) original.body();
                for (int i = 0; i < oidFormBody.size(); i++) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }

                if (!TextUtils.isEmpty(token)) {
                    newFormBody.add(TOKEN_KEY, token);
                }

                request = original.newBuilder().method(original.method(), newFormBody.build()).build();
            }
        }

        assert request != null;
        return chain.proceed(request);
    }
}
