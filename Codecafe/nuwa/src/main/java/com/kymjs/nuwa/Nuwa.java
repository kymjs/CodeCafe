package com.kymjs.nuwa;

import android.content.Context;
import android.util.Log;

import com.kymjs.nuwa.utils.AssetUtils;
import com.kymjs.nuwa.utils.DexUtils;

import java.io.File;
import java.io.IOException;


/**
 * 使用: ./gradlew clean nuwaQihooDebugPatch -P NuwaDir=~/Documents/nuwa
 *
 * @author Created by jixin.jia on 15/10/31.
 */
public class Nuwa {

    private static final String TAG = "nuwa";
    private static final String HACK_DEX = "hack.apk";

    private static final String DEX_DIR = "nuwa";
    private static final String DEX_OPT_DIR = "nuwaopt";

    public static void init(Context context) {
        File dexDir = new File(context.getFilesDir(), DEX_DIR);
        dexDir.mkdir();

        String dexPath = null;
        try {
            dexPath = AssetUtils.copyAsset(context, HACK_DEX, dexDir);
        } catch (IOException e) {
            Log.e(TAG, "copy " + HACK_DEX + " failed");
            e.printStackTrace();
        }

        loadPatch(context, dexPath);
    }

    public static void loadPatch(Context context, String dexPath) {

        if (context == null) {
            Log.e(TAG, "context is null");
            return;
        }
        if (!new File(dexPath).exists()) {
            Log.e(TAG, dexPath + " is null");
            return;
        }
        File dexOptDir = new File(context.getFilesDir(), DEX_OPT_DIR);
        dexOptDir.mkdir();
        try {
            DexUtils.injectDexAtFirst(dexPath, dexOptDir.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "inject " + dexPath + " failed");
            e.printStackTrace();
        }
    }
}
