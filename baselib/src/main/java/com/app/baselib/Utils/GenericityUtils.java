package com.app.baselib.Utils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

/**
 * Created by shen on 2017/5/6.
 * 泛型工具类
 */

public class GenericityUtils {
    /**
     * 得到父类第一个泛型参数
     * @param object 子类本身
     * @return 第一个泛型参数的类型
     */
    public Class getGenericClass(Object object) {
        try {
            Class clazz = (Class) ((ParameterizedType) object.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
            return clazz;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到父类第position个泛型参数
     * @param object 子类本身
     * @return 第position个泛型参数的类型
     */
    public Class getGenericClass(Object object, int position) {
        try {
            Class clazz = (Class) ((ParameterizedType) object.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[position];//得到父类第二个泛型参数
            return clazz;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 检查泛型(不能未抽象或者接口).
     *
     * @param clazz     泛型的额class
     * @param className 泛型主人的名称
     */
    public void checkGenericity(Class clazz, String className) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalStateException("the genericity of " + className +
                    " can not be abstract");
        }

        if (Modifier.isInterface(clazz.getModifiers())) {
            throw new IllegalStateException("the genericity of " + className +
                    " can not be interface");
        }
    }

    public Object instantiationClass(Class clazz) {
        try {
            Object object = clazz.newInstance();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
