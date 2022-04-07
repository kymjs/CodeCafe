package top.codecafe.utils;

import java.io.File;

/**
 * Created by kymjs on 3/2/16.
 */
public final class FileSizeUtil {

    /***
     * 获取文件夹大小
     ***/
    public static long getFileSize(File f) {
        long size = 0L;
        if (f != null && f.exists()) {
            File flist[] = f.listFiles();
            for (File aFlist : flist) {
                size += aFlist.isDirectory() ? getFileSize(aFlist) : aFlist.length();
            }
        }
        return size;
    }
}
