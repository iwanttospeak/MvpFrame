package com.yzl.appres.dialog.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.app.baselib.Utils.ImageDisplayUtil;
import com.app.baselib.Utils.ReminderUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yzl.appres.R;
import com.yzl.appres.bean.ShareBean;
import com.yzl.appres.constant.LibConstant;

/**
 * Created by wang
 */

public class ShareUtil {

    /**
     * 分享到微信.
     */
    public static void shareWx(Activity activity, ShareBean shareBean) {
        realShareWx(activity, shareBean, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享到微信.
     */
    public static void shareFriend(Activity activity, ShareBean shareBean) {
        realShareWx(activity, shareBean, SendMessageToWX.Req.WXSceneTimeline);
    }

    private static void realShareWx(Activity activity, ShareBean shareBean, final int SessionScene) {
        final IWXAPI api = WXAPIFactory.createWXAPI(activity.getApplicationContext(), LibConstant.WX_KEY, true);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareBean.getUrl();

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareBean.getShare_title();
        msg.description = shareBean.getShare_desc();
        ImageDisplayUtil.getImageBitmap(activity, shareBean.getShare_image(), bitmap -> {
            msg.setThumbImage(bitmap);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SessionScene;
            api.sendReq(req);
        });
    }

    /**
     * 分享到新浪.
     */
    public static WbShareHandler shareSina(Activity activity, ShareBean shareBean) {
        AuthInfo authInfo = new AuthInfo(activity, LibConstant.WB_KEY, LibConstant.WB_REDIRECT_URL, LibConstant.WB_SCOPE);
        WbSdk.install(activity.getApplicationContext(), authInfo);

        WbShareHandler wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();

        WeiboMultiMessage message = new WeiboMultiMessage();

        TextObject textObject = new TextObject();
        textObject.text = shareBean.getShare_desc() + shareBean.getUrl();
        textObject.title = shareBean.getShare_title();
        textObject.actionUrl = shareBean.getUrl();
        message.textObject = textObject;

        ImageDisplayUtil.getImageBitmap(activity, shareBean.getShare_image(), bitmap -> {
            ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(bitmap);
            message.imageObject = imageObject;
            wbShareHandler.shareMessage(message, false);
        });

        return wbShareHandler;
    }

    /**
     * 复制链接.
     */
    public static void CopyUrl(Activity activity, ShareBean shareBean) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(shareBean.getShare_title(), shareBean.getUrl()));
        ReminderUtils.showMessage(R.string.share_to_copy_success);
    }
}
