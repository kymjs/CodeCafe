package top.codecafe.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by kymjs on 3/2/16.
 */
public class Tools {

    public static int toInt(String num, int defualt) {
        try {
            return Integer.valueOf(num);
        } catch (NumberFormatException e) {
            return defualt;
        }
    }

    public static long toLong(String num, long defualt) {
        try {
            return Long.valueOf(num);
        } catch (NumberFormatException e) {
            return defualt;
        }
    }

    public static void shareUrl(Context context, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "来自开源实验室的分享:" + url);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "发送到:"));
    }
}
