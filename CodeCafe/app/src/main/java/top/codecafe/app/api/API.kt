package top.codecafe.app.api

import top.codecafe.kjframe.KJHttp
import top.codecafe.kjframe.http.HttpCallBack
import top.codecafe.kjframe.http.HttpParams

/**
 * 应用API

 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
public object API {

    public val HOST: String = ""

    public var kjh: KJHttp = KJHttp()

    public fun getHttpParams(): HttpParams {
        val params = HttpParams()
        // add header
        return params
    }

    public fun getWidgetList(categoty: String, callBack: HttpCallBack) {
        kjh.get("http://www.kymjs.com/api/widget_list", callBack)
    }

    public fun getTopicList(topic: String, callBack: HttpCallBack, pageIndex: Int = 0) {
        val params = getHttpParams()
        params.put("title", topic)
        params.put("pageIndex", pageIndex)
        kjh.get("http://www.oschina.net/action/api/tweet_topic_list", params, callBack)
    }
}
