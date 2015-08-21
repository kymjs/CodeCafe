package top.codecafe.app.ui.widget.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * Base class for ActionBar implementations of {@link MaterialMenuDrawable}
 *
 * @see MaterialMenuIcon
 */
public abstract class MaterialMenuBase implements MaterialMenu {

    private static final String STATE_KEY = "material_menu_icon_state";

    private MaterialMenuDrawable.IconState currentState = MaterialMenuDrawable.IconState.BURGER;

    private MaterialMenuDrawable drawable;

    public MaterialMenuBase(Activity activity, int color, MaterialMenuDrawable.Stroke stroke) {
        this(activity, color, stroke, MaterialMenuDrawable.DEFAULT_TRANSFORM_DURATION, MaterialMenuDrawable.DEFAULT_PRESSED_DURATION);
    }

    public MaterialMenuBase(Activity activity, int color, MaterialMenuDrawable.Stroke stroke, int
            transformDuration) {
        this(activity, color, stroke, transformDuration, MaterialMenuDrawable.DEFAULT_PRESSED_DURATION);
    }

    public MaterialMenuBase(Activity activity, int color, MaterialMenuDrawable.Stroke stroke, int
			transformDuration, int pressedDuration) {
        drawable = new MaterialMenuDrawable(activity, color, stroke, MaterialMenuDrawable.DEFAULT_SCALE, 
				transformDuration, pressedDuration);
        setActionBarSettings(activity);
        if (providesActionBar()) {
            setupActionBar(activity);
        }
    }

    private void setupActionBar(Activity activity) {
        final View iconView = getActionBarHomeView(activity);
        final View upView = getActionBarUpView(activity);

        if (iconView == null || upView == null) {
            throw new IllegalStateException("Could not find ActionBar views");
        }

        // need no margins so that clicked state would work nicely
        ViewGroup.MarginLayoutParams iconParams = (ViewGroup.MarginLayoutParams) iconView
				.getLayoutParams();
        iconParams.bottomMargin = 0;
        iconParams.topMargin = 0;
        iconParams.leftMargin = 0;
        iconView.setLayoutParams(iconParams);

        // remove up arrow margins
        ViewGroup.MarginLayoutParams upParams = (ViewGroup.MarginLayoutParams) upView
				.getLayoutParams();
        upParams.leftMargin = 0;
        upParams.rightMargin = 0;
        upView.setLayoutParams(upParams);
    }

    protected abstract void setActionBarSettings(Activity activity);

    protected abstract View getActionBarHomeView(Activity activity);

    protected abstract View getActionBarUpView(Activity activity);

    protected abstract boolean providesActionBar();

    @Override
    public final void setState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().setIconState(state);
    }

    @Override
    public final MaterialMenuDrawable.IconState getState() {
        return getDrawable().getIconState();
    }

    @Override
    public final void animateState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().animateIconState(state, false);
    }

    @Override
    public final void animatePressedState(MaterialMenuDrawable.IconState state) {
        currentState = state;
        getDrawable().animateIconState(state, true);
    }

    @Override
    public final void setColor(int color) {
        getDrawable().setColor(color);
    }

    @Override
    public final void setTransformationDuration(int duration) {
        getDrawable().setTransformationDuration(duration);
    }

    @Override
    public final void setPressedDuration(int duration) {
        getDrawable().setPressedDuration(duration);
    }

    @Override
    public final void setInterpolator(Interpolator interpolator) {
        getDrawable().setInterpolator(interpolator);
    }

    @Override
    public final void setRTLEnabled(boolean rtlEnabled) {
        getDrawable().setRTLEnabled(rtlEnabled);
    }

    @Override
    public final void setTransformationOffset(MaterialMenuDrawable.AnimationState animationState,
											  float value) {
        currentState = getDrawable().setTransformationOffset(animationState, value);
    }

    @Override
    public final MaterialMenuDrawable getDrawable() {
        return drawable;
    }

    /**
     * Overwrites behaviour of pressed state circle animation even when using
     * {@link #animatePressedState(MaterialMenuDrawable.IconState)}
     *
     * @param neverDrawTouch true to never draw pressed state circle animation
     */
    public final void setNeverDrawTouch(boolean neverDrawTouch) {
        getDrawable().setNeverDrawTouch(neverDrawTouch);
    }

    /**
     * Call from
     * {@link Activity#onSaveInstanceState(Bundle)} to
     * store current icon state
     *
     * @param outState outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_KEY, currentState.name());
    }

    /**
     * Call from {@link Activity#onPostCreate(Bundle)} to
     * restore icon state
     *
     * @param state state
     */
    public void syncState(Bundle state) {
        if (state != null) {
            String iconStateName = state.getString(STATE_KEY);
            if (iconStateName == null) {
                iconStateName = MaterialMenuDrawable.IconState.BURGER.name();
            }
            setState(MaterialMenuDrawable.IconState.valueOf(iconStateName));
        }
    }
}
