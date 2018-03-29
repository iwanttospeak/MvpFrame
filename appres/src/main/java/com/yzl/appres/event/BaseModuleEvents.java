package com.yzl.appres.event;

import android.view.KeyEvent;

/**
 * @author by Wang on 2018/1/9.
 */

public class BaseModuleEvents {


    public static BaseModuleEvents eventsManager;

    public static BaseModuleEvents getIntence(){
        if (eventsManager ==null){
            eventsManager = new BaseModuleEvents();
        }
        return eventsManager;
    }

    public class RecordVideoResult {
        //类型--图片
        public static final String TYPE_IMG = "type_img";
        //类型--视频
        public static final String TYPE_VIDEO = "type_video";
        private String code;
        private String imgPath;
        //视频地址
        private String videoPath;
        private String type;
        public RecordVideoResult(){
        }
        public RecordVideoResult(String code, String imgPath)
        {
            this.imgPath = imgPath;
            this.code = code;
        }
        public RecordVideoResult(String code, String imgPath, String videoPath)
        {
            this.imgPath = imgPath;
            this.code = code;
            this.videoPath = videoPath;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }
    }
//    activity点击keyEvent
    public class DispatchKeyEvent {

        private KeyEvent event;
        public DispatchKeyEvent(KeyEvent event){
            this.event = event;
        }

        public KeyEvent getEvent() {
            return event;
        }

        public void setEvent(KeyEvent event) {
            this.event = event;
        }
    }

    //   手机通讯录数据查询完成事件，在子线程中发布事件
    public class PhoneContactsQueryOverEvent {
        public PhoneContactsQueryOverEvent( ){
        }
    }

}
