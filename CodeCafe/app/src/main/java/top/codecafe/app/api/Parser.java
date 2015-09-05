package top.codecafe.app.api;

import android.util.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import top.codecafe.app.bean.Widget;

/**
 * APP解析类
 *
 * @author kymjs (http://www.kymjs.com/) on 8/28/15.
 */
public class Parser {

    public static <T> T xmlToBean(Class<T> type, String xml) {
        T data = null;
        try {
            XStream xStream = new XStream(new DomDriver("UTF-8"));
            xStream.processAnnotations(type);
            data = (T) xStream.fromXML(xml);
        } catch (Exception e) {
            try {
                data = type.newInstance();
            } catch (Exception ee) {
            } finally {
                Log.e("kymjs", "xml解析异常");
            }
        }
        return data;
    }
    
    public static ArrayList<Widget> parserWidgetList(String json) {
        ArrayList<Widget> datas = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                Widget data = new Widget();
                data.setName(obj.optString("name"));
                datas.add(data);
            }
        } catch (JSONException e) {
        }
        return datas;
    }
}
