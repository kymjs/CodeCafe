/*
 * Copyright (c) 2014, Android Open Source Project,张涛.
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
package com.kymjs.kjcore.http;


import com.kymjs.kjcore.utils.KJLoger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本类工作流程： 每当发起一次Request，会对这个Request标记一个唯一值。<br>
 * 并加入当前请求的Set中(保证唯一;方便控制)。<br>
 * 同时判断是否启用缓存，若启用则加入缓存队列，否则加入执行队列。<br>
 * Note:<br>
 * 整个KJHttp工作流程：采用责任链设计模式，由三部分组成，类似设计可以类比Handle...Looper...MessageQueue<br>
 * 1、KJHttp负责不停向NetworkQueue(或CacheQueue实际还是NetworkQueue， 具体逻辑请查看
 * {@link CacheDispatcher})添加Request<br>
 * 2、另一边由TaskThread不停从NetworkQueue中取Request并交给Network执行器(逻辑请查看
 * {@link NetworkDispatcher} )，<br>
 * 3、Network执行器将执行成功的NetworkResponse返回给TaskThead，并通过Request的定制方法
 * Request.parseNetworkResponse()封装成Response，最终交给分发器 Delivery
 * 分发到主线程并调用HttpCallback相应的方法
 *
 * @author kymjs (https://www.kymjs.com/)
 */
public class KJHttp {

    public interface ContentType {
        int FORM = 0;
        int JSON = 1;
    }

    // 请求缓冲区
    private final Map<String, Queue<Request<?>>> mWaitingRequests = new HashMap<>();
    // 请求的序列化生成器
    private final AtomicInteger mSequenceGenerator = new AtomicInteger();
    // 当前正在执行请求的线程集合
    private final Set<Request<?>> mCurrentRequests = new HashSet<>();
    // 执行缓存任务的队列.
    private final PriorityBlockingQueue<Request<?>> mCacheQueue = new
            PriorityBlockingQueue<>();
    // 需要执行网络请求的工作队列
    private final PriorityBlockingQueue<Request<?>> mNetworkQueue = new
            PriorityBlockingQueue<>();
    // 请求任务执行池
    private final NetworkDispatcher[] mTaskThreads;
    // 缓存队列调度器
    private CacheDispatcher mCacheDispatcher;
    // 配置器
    private HttpConfig mConfig;

    public KJHttp(HttpConfig config) {
        if (config == null) {
            config = new HttpConfig();
        }
        this.mConfig = config;
        mConfig.mController.setRequestQueue(this);
        mTaskThreads = new NetworkDispatcher[HttpConfig.NETWORK_POOL_SIZE];
        start();
    }

    /**
     * 执行一个自定义请求
     *
     * @param request 要执行的自定义请求
     */
    public void doRequest(Request<?> request) {
        request.setConfig(mConfig);
        add(request);
    }

    public HttpConfig getConfig() {
        return mConfig;
    }


    public void setConfig(HttpConfig config) {
        this.mConfig = config;
    }

    /******************************** core method ****************************************/

    /**
     * 启动队列调度
     */
    private void start() {
        stop();// 首先关闭之前的运行，不管是否存在
        mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue,
                mConfig);
        mCacheDispatcher.start();
        // 构建线程池
        for (int i = 0; i < mTaskThreads.length; i++) {
            NetworkDispatcher tasker = new NetworkDispatcher(mNetworkQueue,
                    mConfig.mNetwork, HttpConfig.mCache, mConfig.mDelivery);
            mTaskThreads[i] = tasker;
            tasker.start();
        }
    }

    /**
     * 停止队列调度
     */
    private void stop() {
        if (mCacheDispatcher != null) {
            mCacheDispatcher.quit();
        }
        for (NetworkDispatcher thread : mTaskThreads) {
            if (thread != null) {
                thread.quit();
            }
        }
    }

    public void cancel(String url) {
        synchronized (mCurrentRequests) {
            for (Request<?> request : mCurrentRequests) {
                if (url.equals(request.getTag())) {
                    request.cancel();
                }
            }
        }
    }

    /**
     * 取消全部请求
     */
    public void cancelAll() {
        synchronized (mCurrentRequests) {
            for (Request<?> request : mCurrentRequests) {
                request.cancel();
            }
        }
    }

    /**
     * 向请求队列加入一个请求
     * Note:此处工作模式是这样的：KJHttp可以看做是一个队列类，而本方法不断的向这个队列添加request；另一方面，
     * TaskThread不停的从这个队列中取request并执行。类似的设计可以参考Handle...Looper...MessageQueue的关系
     */
    public <T> Request<T> add(Request<T> request) {
        if (request.getCallback() != null) {
            request.getCallback().onPreStart();
        }

        // 标记该请求属于该队列，并将它添加到该组当前的请求。
        request.setRequestQueue(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }
        // 设置进程优先序列
        request.setSequence(mSequenceGenerator.incrementAndGet());

        // 如果请求不可缓存，跳过缓存队列，直接进入网络。
        if (!request.shouldCache()) {
            mNetworkQueue.add(request);
            return request;
        }

        // 如果已经在mWaitingRequests中有本请求，则替换
        synchronized (mWaitingRequests) {
            String cacheKey = request.getCacheKey();
            if (mWaitingRequests.containsKey(cacheKey)) {
                // There is already a request in flight. Queue up.
                Queue<Request<?>> stagedRequests = mWaitingRequests
                        .get(cacheKey);
                if (stagedRequests == null) {
                    stagedRequests = new LinkedList<Request<?>>();
                }
                stagedRequests.add(request);
                mWaitingRequests.put(cacheKey, stagedRequests);
                if (HttpConfig.DEBUG) {
                    KJLoger.debug(String.format("Request for cacheKey=%s is in flight, putting on" +
                            " hold.", cacheKey));
                }
            } else {
                mWaitingRequests.put(cacheKey, null);
                mCacheQueue.add(request);
            }
            return request;
        }
    }

    /**
     * 将一个请求标记为已完成
     */
    public void finish(Request<?> request) {
        synchronized (mCurrentRequests) {
            mCurrentRequests.remove(request);
        }

        if (request.shouldCache()) {
            synchronized (mWaitingRequests) {
                String cacheKey = request.getCacheKey();
                Queue<Request<?>> waitingRequests = mWaitingRequests.remove(cacheKey);
                if (waitingRequests != null) {
                    if (HttpConfig.DEBUG) {
                        KJLoger.debug(String.format("Releasing %d waiting requests for " +
                                "cacheKey=%s.", waitingRequests.size(), cacheKey));
                    }
                    mCacheQueue.addAll(waitingRequests);
                }
            }
        }
    }

    /**
     * 获取缓存数据
     *
     * @param url 哪条url的缓存
     * @return 缓存的二进制数组
     */
    public static byte[] getCache(String url) {
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
     * 移除一个缓存
     *
     * @param url 哪条url的缓存
     */
    public static void removeCache(String url) {
        HttpConfig.mCache.remove(url);
    }

    /**
     * 清空缓存
     */
    public static void cleanCache() {
        HttpConfig.mCache.clean();
    }

    public void destroy() {
        cancelAll();
        stop();
    }
}
