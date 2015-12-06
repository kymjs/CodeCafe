package com.kymjs.base.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 动态修改一个View的margintop
 *
 * @author kymjs (http://www.kymjs.com/) on 12/1/15.
 */
public class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
    private LayoutParams contentParams;
    private int maxHeight;
    private View view;

    public ScrollTask(View view, int maxHeight) {
        contentParams = (LayoutParams) view.getLayoutParams();
        this.maxHeight = -maxHeight;
        this.view = view;
    }

    @Override
    protected Integer doInBackground(Integer... speed) {
        int topMargin = contentParams.topMargin;
        // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
        while (true) {
            topMargin += speed[0];
            if (topMargin < maxHeight) {
                topMargin = maxHeight;
                break;
            }
            if (topMargin > 0) {
                topMargin = 0;
                break;
            }
            publishProgress(topMargin);
            // 16毫秒以内最好
            sleep(10);
        }
        return topMargin;
    }

    @Override
    protected void onProgressUpdate(Integer... top) {
        setTopMargin(top[0]);
    }

    @Override
    protected void onPostExecute(Integer top) {
        setTopMargin(top);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setTopMargin(float topMargin) {
        contentParams.topMargin = (int) topMargin;
        view.setLayoutParams(contentParams);
        view.invalidate();
    }
}
