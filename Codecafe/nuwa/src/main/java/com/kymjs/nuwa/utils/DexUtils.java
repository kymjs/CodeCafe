package com.kymjs.nuwa.utils;

import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;

/**
 * Created by jixin.jia on 15/10/31.
 */
public class DexUtils {

    public static void injectDexAtFirst(String dexPath, String defaultDexOptPath) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, defaultDexOptPath, dexPath, getPathClassLoader());
        Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));
        Object newDexElements = getDexElements(getPathList(dexClassLoader));
        Object allDexElements = combineArray(newDexElements, baseDexElements);
        Object pathList = getPathList(getPathClassLoader());
        
        //将pathlist的dexElements设置为allDexElements
        //即把当前应用的classloader中的pathlist属性的dexElements设置为拼接后的dexElements
        ReflectionUtils.setField(pathList, pathList.getClass(), "dexElements", allDexElements);
    }

    private static ClassLoader getPathClassLoader() {
        return DexUtils.class.getClassLoader();
    }

    /**
     * 返回 paramObject 对象中名为dexElements的字段
     */
    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        return ReflectionUtils.getField(paramObject, paramObject.getClass(), "dexElements");
    }

    /**
     * 返回 baseDexClassLoader 对象中名为pathList的字段
     */
    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return ReflectionUtils.getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 将两个DexElements数组拼接成一个DexElement数组,后一个直接拼接到前一个上
     */
    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }

}
