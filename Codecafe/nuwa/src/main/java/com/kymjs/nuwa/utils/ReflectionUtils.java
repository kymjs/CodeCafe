package com.kymjs.nuwa.utils;

import java.lang.reflect.Field;

/**
 * Created by jixin.jia on 15/10/31.
 */
public class ReflectionUtils {
    /**
     * 如果obj是clazz类的对象,返回这个obj对象中的field字段 
     * 否则抛异常
     */
    public static Object getField(Object obj, Class<?> clazz, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //反射类中的属性
        Field localField = clazz.getDeclaredField(field);
        //设置可以访问到私有属性
        localField.setAccessible(true);
        //获取到obj对象中的localField属性
        return localField.get(obj);
    }

    /**
     * 如果obj是clazz类的对象,将obj的field字段设置为value值
     */
    public static void setField(Object obj, Class<?> clazz, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //反射类中的属性
        Field localField = clazz.getDeclaredField(field);
        //设置可以访问到私有属性
        localField.setAccessible(true);
        //将obj对象的field字段设置为value值
        localField.set(obj, value);
    }
}
