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

package top.codecafe.kjframe.http;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Http响应的分发器，这里用于把异步线程中的响应分发到UI线程中执行
 */
public class DeliveryExecutor implements Delivery {

    private final Executor mResponsePoster;

    public DeliveryExecutor(final Handler handler) {
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public DeliveryExecutor(Executor executor) {
        mResponsePoster = executor;
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        postResponse(request, response, null);
    }

    /**
     * 当有中介响应的时候，会被调用，首先返回中介响应，并执行runnable(实际就是再去请求网络)<br>
     */
    @Override
    public void postResponse(Request<?> request, Response<?> response,
                             Runnable runnable) {
        request.markDelivered();
        if (response.isSuccess()) {
            request.onAsyncSuccess(response.result);
        }
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response,
                runnable));
    }

    @Override
    public void postError(Request<?> request, KJHttpException error) {
        Response<?> response = Response.error(error);
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response,
                null));
    }

    /**
     * 一个Runnable，将网络请求响应分发到UI线程中
     */
    @SuppressWarnings("rawtypes")
    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Response mResponse;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, Response response,
                                        Runnable runnable) {
            mRequest = request;
            mResponse = response;
            mRunnable = runnable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if (mRequest.isCanceled()) {
                mRequest.finish("request已经取消，在分发时finish");
                return;
            }

            if (mResponse.isSuccess()) {
                mRequest.deliverResponse(mResponse.headers, mResponse.result);
            } else {
                mRequest.deliverError(mResponse.error);
            }
            mRequest.requestFinish();
            mRequest.finish("done");
            if (mRunnable != null) { // 执行参数runnable
                mRunnable.run();
            }
        }
    }

    @Override
    public void postDownloadProgress(Request<?> request, long fileSize,
                                     long downloadedSize) {
        request.mCallback.onLoading(fileSize, downloadedSize);
    }

    @Override
    public void postCancel(Request<?> request) {
    }
}
