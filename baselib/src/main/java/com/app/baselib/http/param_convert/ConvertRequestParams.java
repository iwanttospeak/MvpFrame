package com.app.baselib.http.param_convert;

import android.support.v4.util.ArrayMap;

import com.app.baselib.annotation.FieldProp;
import com.app.baselib.http.params.BaseParams;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/**
 * @author by admin on 2017/4/20.
 * 此类用来从无法确定的请求参数对象中将对应的此次请求的参数提取出来
 * 以登录请求为例，意思就是因为不同的APP可能同一个登录请求所需的参数也是不同
 * 例如有些只需要phone，password，而有些就需要昵称nickName,邮箱等，那么很显然我们不能使用同一个请求对象
 * 因此我们的请求对象是不确定的，那么问题来了这么多不同的请求对象，我们不知道此对象里面会有什么参数，
 * 那么我们就使用反射的方法将此对象中的字段反射出来，并且获取到其值然后全部设置到登录请求的接口参数里面，
 * 也就是说我们model仅仅只是作为一个中间站，获取参数之后不做什么修改就将其提交到服务器上，这样就可以做到
 * 一个请求model对应一个服务器接口，但是又可以对应多个view
 */

public class ConvertRequestParams {

    /**
     * 将Module类型转成keyMap
     * @param baseParams 源Module
     * @return map
     */
    public static Map<String,String> moduleToRequestParams(BaseParams baseParams) {

        Map<String,String> map = new ArrayMap<>();
        Class tempClass = baseParams.getClass();//当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null  && !tempClass.getName().toLowerCase().equals("java.lang.object")) { //屏蔽掉object的影响
            final Field[] fields = tempClass.getDeclaredFields();//得到被声明的private/protected/public字段,field是final类型，不能进行转换
            AccessController.doPrivileged(new PrivilegedAction<Void>() {//设置访问控制特权,设置访问特权用于提高反射速度
                public Void run() {
                    AccessibleObject.setAccessible(fields, true);//设置不进行代码检查
                    return null;
                }
            });
            String fieldName;
            FieldProp fp;
            try {
                for (Field field : fields) {
                    fp = field.getAnnotation(FieldProp.class);
                    if (fp == null || fp.skip()) {
                        continue;
                    }
                    fieldName = field.getName();//返回由该字段表示的字段名称，获取属性声明时名字
                    try {
                        if (ReflectUtil.isString(field)) {
                            String param = field.get(baseParams) == null ? "" : (String) field.get(baseParams);
                            map.put(fieldName, param);//取得此对象上这个Field上的值

                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return map;
    }

//    /**
//     * 将Module类型转成ContentValues
//     *
//     * @param module 源Module
//     * @return ContentValues
//     * @author shenghua.lin
//     */
//    private ContentValues moduleToContentValues(final BaseParams module) {
//
//        ContentValues values = new ContentValues();
//        final Field[] fields = module.getClass().getDeclaredFields();
//        AccessController.doPrivileged(new PrivilegedAction<Void>() {
//            public Void run() {
//                AccessibleObject.setAccessible(fields, true);
//                return null;
//            }
//        });
//
//        String fieldName;
//        FieldProp fp;
//        try {
//            for (Field field : fields) {
//                fp = field.getAnnotation(FieldProp.class);
//
//                if (fp == null || (fp.skip())) {
//                    continue;
//                }
//
//                fieldName = field.getName();
//                if ("updateType".equals(fieldName)) {
//                    continue;
//                }
//                try {
//                    if (ReflectUtil.isString(field)) {
//                        values.put(fieldName, field.get(module) == null ? "" : (String) field.get(module));
//                    } else if (ReflectUtil.isInt(field)) {
//                        values.put(fieldName, field.getInt(module));
//                    } else if (ReflectUtil.isFloat(field)) {
//                        values.put(fieldName, field.getFloat(module));
//                    } else if (ReflectUtil.isBoolean(field)) {
//                        values.put(fieldName, field.getBoolean(module) + "");
//                    } else {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    continue;
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return values;
//    }
}
