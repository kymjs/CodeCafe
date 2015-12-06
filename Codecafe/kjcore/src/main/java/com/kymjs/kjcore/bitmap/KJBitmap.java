/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kymjs.kjcore.bitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.kymjs.kjcore.http.Cache;
import com.kymjs.kjcore.http.HttpCallBack;
import com.kymjs.kjcore.http.HttpConfig;
import com.kymjs.kjcore.http.KJHttp;
import com.kymjs.kjcore.utils.SystemTool;

import java.util.HashSet;

/**
 * The BitmapLibrary's core classes<br>
 * <b>创建时间</b> 2014-6-11<br>
 * <b>最后修改</b> 2015-12-16<br>
 *
 * @author kymjs (https://github.com/kymjs)
 * @version 2.4
 */
public class KJBitmap {

    private ImageDisplayer displayer;
    private DiskImageRequest diskImageRequest;
    private HashSet<String> currentUrls = new HashSet<>(20);

    public KJBitmap(BitmapConfig bitmapConfig) {
        this(null, bitmapConfig);
    }

    public KJBitmap(KJHttp kjHttp, BitmapConfig bitmapConfig) {
        if (kjHttp == null) kjHttp = new KJHttp(null);
        if (bitmapConfig == null) bitmapConfig = new BitmapConfig();
        if (BitmapConfig.mMemoryCache == null) BitmapConfig.mMemoryCache = new BitmapMemoryCache();
        displayer = new ImageDisplayer(kjHttp, bitmapConfig);
    }

    /**
     * 真正去加载一个图片
     */
    public void doDisplay(final View imageView, final String imageUrl, int width, int height,
                          final Drawable loadBitmap, final int loadBitmapRes,
                          final Drawable errorBitmap, final int errorBitmapRes,
                          final HttpCallBack callback) {
        imageView.setTag(imageUrl);
        HttpCallBack bitmapCallBack = new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                if (getMemoryCache(imageUrl) == null)
                    setImageWithResource(imageView, loadBitmap, loadBitmapRes);
                if (callback != null)
                    callback.onPreStart();
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                super.onSuccess(bitmap);
                if (imageUrl.equals(imageView.getTag())) {
                    doSuccess(imageView, bitmap, errorBitmap, errorBitmapRes);
                    if (callback != null)
                        callback.onSuccess(bitmap);
                    currentUrls.add(imageUrl);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                doFailure(imageView, errorBitmap, errorBitmapRes);
                if (callback != null) {
                    callback.onFailure(errorNo, strMsg);
                }
            }

            @Override
            public void onFinish() {
                if (callback != null) {
                    callback.onFinish();
                }
            }

            @Override
            public void onPreHttp() {
                if (callback != null) {
                    callback.onPreHttp();
                }
            }
        };

        if (imageUrl.startsWith("http")) {
            displayer.get(imageUrl, width, height, bitmapCallBack);
        } else {
            if (diskImageRequest == null) {
                diskImageRequest = new DiskImageRequest();
            }
            diskImageRequest.load(imageUrl, width, height, bitmapCallBack);
        }
    }

    /**
     * 如果内存缓存有图片，则显示内存缓存的图片，否则显示默认图片
     *
     * @param imageView    要显示的View
     * @param imageUrl     网络图片地址
     * @param defaultImage 如果没有内存缓存，则显示默认图片
     */
    public void displayCacheOrDefult(View imageView, String imageUrl,
                                     int defaultImage) {
        Bitmap cache = getMemoryCache(imageUrl);
        doSuccess(imageView, cache, null, defaultImage);
    }

    /**
     * 如果内存缓存有图片，则显示内存缓存的图片，否则显示默认图片
     *
     * @param imageView    要显示的View
     * @param imageUrl     网络图片地址
     * @param defaultImage 如果没有内存缓存，则显示默认图片
     */
    public void displayCacheOrDefult(View imageView, String imageUrl,
                                     Drawable defaultImage) {
        Bitmap cache = getMemoryCache(imageUrl);
        doSuccess(imageView, cache, defaultImage, 0);
    }

    /**
     * 移除一个缓存
     *
     * @param url 哪条url的缓存
     */
    public void removeCache(String url) {
        BitmapConfig.mMemoryCache.remove(url);
        HttpConfig.mCache.remove(url);
    }

    public void destroy() {
        for (String url : currentUrls) {
            BitmapConfig.mMemoryCache.remove(url);
        }
        currentUrls = null;
        displayer = null;
        diskImageRequest = null;
    }

    /**
     * 取消一个加载请求
     *
     * @param url 要取消的url
     */
    public void cancel(String url) {
        displayer.cancel(url);
    }

    /**
     * 清空缓存
     */
    public void cleanCache() {
        BitmapConfig.mMemoryCache.clean();
        HttpConfig.mCache.clean();
    }

    /**
     * 获取缓存数据
     *
     * @param url 哪条url的缓存
     * @return 缓存数据的二进制数组
     */
    public byte[] getCache(String url) {
        Cache cache = HttpConfig.mCache;
        cache.initialize();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            return entry.data;
        } else {
            return new byte[0];
        }
    }

    /**
     * 获取内存缓存
     *
     * @param url key
     * @return 缓存的bitmap或null
     */
    public static Bitmap getMemoryCache(String url) {
        return BitmapConfig.mMemoryCache.getBitmap(url);
    }

    /*********************
     * private method
     *********************/

    /**
     * 按照优先级为View设置图片资源
     * 优先使用drawable，仅当drawable无效时使用bitmapRes，若两值均无效，则不作处理
     *
     * @param view          要设置图片的控件(View设置bg，ImageView设置src)
     * @param errorImage    优先使用项
     * @param errorImageRes 次级使用项
     */
    public static void doFailure(View view, Drawable errorImage, int errorImageRes) {
        setImageWithResource(view, errorImage, errorImageRes);
    }

    /**
     * 按照优先级为View设置图片资源
     *
     * @param view          要设置图片的控件(View设置bg，ImageView设置src)
     * @param bitmap        优先使用项
     * @param errorImage    二级使用项
     * @param errorImageRes 三级使用项
     */
    public static void doSuccess(View view, Bitmap bitmap, Drawable errorImage,
                                 int errorImageRes) {
        if (bitmap != null) {
            setViewImage(view, bitmap);
        } else {
            setImageWithResource(view, errorImage, errorImageRes);
        }
    }

    /**
     * 按照优先级为View设置图片资源
     * 优先使用drawable，仅当drawable无效时使用bitmapRes，若两值均无效，则不作处理
     *
     * @param imageView 要设置图片的控件(View设置bg，ImageView设置src)
     * @param drawable  优先使用项
     * @param bitmapRes 次级使用项
     */
    private static void setImageWithResource(View imageView, Drawable drawable,
                                             int bitmapRes) {
        if (drawable != null) {
            setViewImage(imageView, drawable);
        } else if (bitmapRes > 0) { //大于0视为有效ImageResource
            setViewImage(imageView, bitmapRes);
        }
    }

    private static void setViewImage(View view, int background) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(background);
        } else {
            view.setBackgroundResource(background);
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private static void setViewImage(View view, Bitmap background) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(background);
        } else {
            if (SystemTool.getSDKVersion() >= 16) {
                view.setBackground(new BitmapDrawable(view.getResources(),
                        background));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(view
                        .getResources(), background));
            }
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private static void setViewImage(View view, Drawable background) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(background);
        } else {
            if (SystemTool.getSDKVersion() >= 16) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }
}
