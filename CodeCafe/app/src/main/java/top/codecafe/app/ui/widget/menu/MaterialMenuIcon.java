package top.codecafe.app.ui.widget.menu;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import top.codecafe.app.R;

import static top.codecafe.app.ui.widget.menu.MaterialMenuDrawable.Stroke;

/**
 * A helper class for implementing {@link MaterialMenuDrawable} as an
 * {@link ActionBar} icon.
 * <p/>
 * In order to preserve default ActionBar icon click state call
 * {@link MaterialMenuBase#setNeverDrawTouch(boolean)}. Otherwise, adjust your
 * theme to disable pressed background color by setting
 * <code>android:actionBarItemBackground</code> to null and use
 * <code>android:actionButtonStyle</code>,
 * <code>android:actionOverflowButtonStyle</code> to enable other menu icon
 * backgrounds.
 * <p/>
 * Disables ActionBar Up arrow and replaces default drawable using
 * {@link ActionBar#setIcon(android.graphics.drawable.Drawable)}
 */
public class MaterialMenuIcon extends MaterialMenuBase {

    public MaterialMenuIcon(Activity activity, int color, Stroke stroke) {
        super(activity, color, stroke);
    }

    public MaterialMenuIcon(Activity activity, int color, Stroke stroke, int transformDuration) {
        super(activity, color, stroke, transformDuration);
    }

    public MaterialMenuIcon(Activity activity, int color, Stroke stroke, int transformDuration,
                            int pressedDuration) {
        super(activity, color, stroke, transformDuration, pressedDuration);
    }


    @Override
    protected View getActionBarUpView(Activity activity) {
        ImageView backImg = (ImageView) activity.findViewById(R.id.titlebar_img_back);
        return backImg;
    }

    @Override
    protected View getActionBarHomeView(Activity activity) {
        ImageView backImg = (ImageView) activity.findViewById(R.id.titlebar_img_back);
        return backImg;
    }
    
//    @Override
//    protected View getActionBarHomeView(Activity activity) {
//        Resources resources = activity.getResources();
//        return activity.getWindow().getDecorView().findViewById(resources.getIdentifier
//                ("android:id/home", null, null));
//    }

//    @Override
//    protected View getActionBarUpView(Activity activity) {
//        Resources resources = activity.getResources();
//        ViewGroup actionBarView = (ViewGroup) activity.getWindow().getDecorView().findViewById
//                (resources.getIdentifier("android:id/action_bar", null, null));
//        View homeView = actionBarView.getChildAt(actionBarView.getChildCount() > 1 ? 1 : 0);
//        return homeView.findViewById(resources.getIdentifier("android:id/up", null, null));
//    }

    @Override
    protected boolean providesActionBar() {
        return true;
    }

    @Override
    @TargetApi(14)
    protected void setActionBarSettings(Activity activity) {
        ImageView backImg = (ImageView) activity.findViewById(R.id.titlebar_img_back);
        if (backImg != null) {
            backImg.setImageDrawable(getDrawable());
        }
    }
}
