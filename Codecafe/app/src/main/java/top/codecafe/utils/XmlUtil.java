package top.codecafe.utils;

import com.kymjs.rxvolley.toolbox.FileUtils;
import com.kymjs.rxvolley.toolbox.Loger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * xml解析工具类
 */
public class XmlUtil {

    private final static String TAG = XmlUtil.class.getSimpleName();

    /**
     * 将一个xml流转换为bean实体类
     *
     * @param type
     * @param is
     * @return
     */
    @SuppressWarnings("unchecked")
    public synchronized static <T> T toBean(Class<T> type, InputStream is) {
        XStream xmStream = new XStream(new DomDriver());
        // 设置可忽略为在javabean类中定义的界面属性
        xmStream.ignoreUnknownElements();
        xmStream.processAnnotations(type);
        T obj = null;
        try {
            obj = (T) xmStream.fromXML(is);
        } catch (Exception e) {
            Loger.debug("===解析xml发生异常：" + e.getMessage());
        } finally {
            FileUtils.closeIO(is);
        }
        return obj;
    }

    public static <T> T toBean(Class<T> type, byte[] bytes) {
        if (bytes == null) return null;
        return toBean(type, new ByteArrayInputStream(bytes));
    }
}
