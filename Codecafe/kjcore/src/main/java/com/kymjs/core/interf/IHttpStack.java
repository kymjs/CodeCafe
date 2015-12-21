package com.kymjs.core.interf;


import com.kymjs.core.http.Request;
import com.kymjs.core.http.URLHttpResponse;
import com.kymjs.core.toolbox.HttpParamsEntry;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Http请求端定义
 *
 * @author kymjs (http://www.kymjs.com/) on 12/17/15.
 * @see com.kymjs.core.http.HttpConnectStack
 */
public interface IHttpStack {

    /**
     * 让Http请求端去发起一个Request
     *
     * @param request           一次实际请求集合
     * @param additionalHeaders Http请求头
     * @return 一个Http响应
     */
    URLHttpResponse performRequest(Request<?> request, ArrayList<HttpParamsEntry> additionalHeaders)
            throws IOException;

}