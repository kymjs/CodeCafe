package top.codecafe.app.api;

import top.codecafe.kjframe.KJHttp;
import top.codecafe.kjframe.http.HttpParams;

/**
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
public class API {

    public static final String HOST = "";

    public static KJHttp kjh = new KJHttp();
    
    public static HttpParams getHttpParams() {
        HttpParams params = new HttpParams();
        // add header
        return params;
    }
    
}
