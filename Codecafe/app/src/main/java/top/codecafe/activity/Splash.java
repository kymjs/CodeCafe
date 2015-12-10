package top.codecafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.kymjs.kjcore.Core;

import top.codecafe.R;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = new View(this);
        view.setBackgroundResource(R.mipmap.git_star);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(800);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.initialize();
    }

    private void redirectTo() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
