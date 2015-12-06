package com.kymjs.nuwa.utils;

import java.lang.reflect.Field;

/**
 * Created by jixin.jia on 15/10/31.
 */
public class ReflectionUtils {
    public static Object getField(Object obj, Class<?> clazz, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static void setField(Object obj, Class<?> clazz, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
