package com.laojiang.imagepickers.data;

/**
 * 选择模式
 * ONLY_CAMERA：代表只需要相机入口,用来执行拍照，当设置为此模式的时候，只有执行剪裁或者不剪裁两种操作，其他设置会被忽略
 * 单选或者相机模式下支持剪切，多选不支持
 *  SINGLE：代表单选
 *  单选模式下默认没有相机入口
 *  MUTIL：代表多选
 *  多选模式下默认没有相机入口
 */
public enum ImagePickType {
    ONLY_CAMERA, SINGLE, MUTIL
}
