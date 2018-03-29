package com.yzl.moduleforum;

import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.bean.WrapDataNoContent;
import com.yzl.appres.bean.LoginBean;
import com.yzl.moduleforum.params.ReleaseDynamicParams;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author by Wang
 */

public interface ForumApiService {

    /**
     * 发布帖子
     */
    @FormUrlEncoded
    @POST("forum/post")
    Observable<WrapDataNoContent> forumPost(@Body ReleaseDynamicParams params);

}
