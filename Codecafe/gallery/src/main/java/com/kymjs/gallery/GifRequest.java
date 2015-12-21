/*
 * Copyright (c) 2015, 张涛.
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
package com.kymjs.gallery;


import com.kymjs.core.bitmap.client.BitmapRequestConfig;
import com.kymjs.core.client.HttpCallback;
import com.kymjs.core.http.HttpHeaderParser;
import com.kymjs.core.http.NetworkResponse;
import com.kymjs.core.http.Request;
import com.kymjs.core.http.Response;
import com.kymjs.core.http.VolleyError;
import com.kymjs.core.interf.IPersistence;
import com.kymjs.core.toolbox.HttpParamsEntry;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 让KJBitmap兼容GifView
 *
 * @author kymjs (http://www.kymjs.com/) on 10/13/15.
 */
public class GifRequest extends Request<byte[]> implements IPersistence {

    // 用来保证当前对象只有一个线程在访问
    private static final Object sDecodeLock = new Object();

    public GifRequest(BitmapRequestConfig config, HttpCallback callback) {
        super(config, callback);
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public String getCacheKey() {
        return getUrl();
    }

    @Override
    public ArrayList<HttpParamsEntry> getHeaders() {
        return ((BitmapRequestConfig) getConfig()).getHeaders();
    }

    @Override
    public Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        synchronized (sDecodeLock) {
            try {
                return doParse(response);
            } catch (OutOfMemoryError e) {
                return Response.error(new VolleyError(e));
            }
        }
    }

    @Override
    protected void deliverResponse(ArrayList<HttpParamsEntry> headers, byte[] response) {
        if (mCallback != null) {
            HashMap<String, String> map = new HashMap<>(headers.size());
            for (HttpParamsEntry entry : headers) {
                map.put(entry.k, entry.v);
            }
            mCallback.onSuccess(map, response);
        }
    }

    private Response<byte[]> doParse(NetworkResponse response) {
        if (response.data == null) {
            return Response.error(new VolleyError(response));
        } else {
            return Response.success(response.data, response.headers,
                    HttpHeaderParser.parseCacheHeaders(getUseServerControl(), getCacheTime(),
                            response));
        }
    }
}
