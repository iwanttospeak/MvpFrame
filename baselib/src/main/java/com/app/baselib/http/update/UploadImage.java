package com.app.baselib.http.update;

import android.content.Context;

import com.app.baselib.Utils.GsonUtils;
import com.app.baselib.Utils.StringUtil;
import com.app.baselib.bean.QNTokenBean;
import com.app.baselib.http.Api;
import com.app.baselib.http.BaseApiService;
import com.app.baselib.http.bean.UpdateImgResultBean;
import com.app.baselib.http.bean.WrapDataBean;
import com.blankj.utilcode.util.ToastUtils;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;


/**
 * 七牛上传图片工具类
 * @author by admin on 2017/3/23.
 */
public class UploadImage {

//    private Context mContext;

    private List<File> mFiles;

    private Consumer<String> tokenAction;

    private SuccessListener successListener;
    private SuccessResultListener successResultListener;

    private ErrorListener errorListener;
    private Context context;
    public UploadImage(List<File> files) {
        mFiles = files;
    }

    /** 上传单张、多张图片 **/
    public void upload(SuccessListener listener, Context context) {
        successListener = listener;
        initToken(context);
        tokenAction = s -> uploadImage(s,false);
    }
    /** 上传单张、多张图片 **/
    public void upload(SuccessResultListener listener, Context context) {
        this.context = context;
        initToken(context);
        tokenAction = s -> uploadImage(s,true);
    }
    private void initToken(Context context) {
        this.context = context;
        getQnToken();
    }

    /** 网络错误 **/
    public UploadImage networkError(ErrorListener listener){
        errorListener = listener;
        return this;
    }

    /** 获取QNToken **/
    private void getQnToken() {
        //get请求
        Map<String,String> params = new HashMap<>();
        Api.getInstance().getApiService(BaseApiService.class)
                .getUploadToken(params)
                .subscribeOn(Schedulers.io())//subscribeOn的调用切换之前的线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WrapDataBean<QNTokenBean>>() {
                    @Override
                    public void accept(WrapDataBean<QNTokenBean> wrapDataNoContent) throws Exception {
                        if (wrapDataNoContent.getErrcode() == 0) {
                            String token =wrapDataNoContent.getContent().getToken();
                            Observable.just(token).subscribe(tokenAction);
                        } else {
                            ToastUtils.showShort("获取七牛token失败");
                            netWorkCallBack();
                        }
                    }
                });
    }

    /** 上传图片 **/
    private void uploadImage(String token,boolean isSelfHandle) {
        List<String> images = new ArrayList<>();
        List<String> count = new ArrayList<>();
        List<UpdateImgResultBean> resultList = new ArrayList<>();

        Observable.fromIterable(mFiles).subscribe(file -> {//循环上传
            //指定七牛服务上的文件名，或 null
            String key = System.currentTimeMillis() + "" + new Random().nextInt(10000)+ ".jpg";
            images.add(key);
            //七牛开始上传
            UploadManager manager = new UploadManager();
            manager.put(file, key, token, (key1, info, result) -> {
                        //上传成功
                        if (info.isOK()) {
                            count.add("1");
                            if (isSelfHandle){//自行处理结果
                                selfHandle(count,result,resultList);
                            }else {
                                handleResult(count,images);
                            }
                        } else {
                            resultCallBack(false,null, info.error);
                        }
                    }, null);
        });
    }

    private void handleResult(List<String> count,List<String> images) {
        if (count.size() >= mFiles.size()) { //全部上传成功调用回调方法
            resultCallBack(true, images,null);
        }
    }

    //自行处理结果
    private void selfHandle(List<String> count,JSONObject result, List<UpdateImgResultBean> resultList) {
        String json = result.toString();
        UpdateImgResultBean bean = GsonUtils.getObject(json,UpdateImgResultBean.class);
        resultList.add(bean);
        if (count.size() >= mFiles.size()) { //全部上传成功调用回调方法
            resultHandleCallBack(true,resultList,null);
        }
    }

    /** 回调 **/
    private void resultCallBack(boolean isSuccess, List<String> imgList,String error){
        if (successListener != null){
            successListener.onSuccess(isSuccess, imgList,error);
        }
    }
    /** 回调 **/
    private void resultHandleCallBack(boolean isSuccess, List<UpdateImgResultBean> result,String error){
        if (successResultListener != null){
            successResultListener.onSuccess(isSuccess, result,error);
        }
    }
    private void netWorkCallBack(){
        if (errorListener != null){
            errorListener.onError();
        }
    }

    public interface SuccessListener{
        void onSuccess(boolean isSuccess, List<String> result,String error);
    }
    public interface SuccessResultListener{
        void onSuccess(boolean isSuccess, List<UpdateImgResultBean> result, String error);
    }

    public interface ErrorListener{
        void onError();
    }

}
