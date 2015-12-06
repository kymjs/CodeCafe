package com.kymjs.nuwa.utils;

import android.content.Context;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jixin.jia on 15/10/31.
 *
 * @author kymjs (http://www.kymjs.com/) on 11/17/15.
 */
public class AssetUtils {

    /**
     * 复制文件
     *
     * @param assetName 存在于/assets中的文件名
     * @param dir       将要被复制到的文件夹路径
     * @return 复制后的文件的绝对路径
     * @throws IOException
     */
    public static String copyAsset(Context context, String assetName, File dir) throws IOException {
        File outFile = new File(dir, assetName);
        if (!outFile.exists()) {
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = context.getAssets().open(assetName);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } finally {
                closeIO(in, out);
            }
        }
        return outFile.getAbsolutePath();
    }

    private static void copyFile(InputStream in, FileOutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * 关闭流
     */
    public static void closeIO(Closeable... closeables) throws IOException {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            if (null != cb) {
                cb.close();
            }
        }
    }
}
