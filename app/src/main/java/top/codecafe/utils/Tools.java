package top.codecafe.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

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
        shareUrl(context, "", url);
    }

    public static void shareUrl(Context context, String title, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String content = "来自开源实验室的分享:";
        if (!TextUtils.isEmpty(title)) {
            content += "\n《";
            content += title;
            content += "》\n";
        }
        content += url;
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "发送到:"));
    }
}
