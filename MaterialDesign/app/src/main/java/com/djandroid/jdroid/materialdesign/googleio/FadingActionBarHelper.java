package com.djandroid.jdroid.materialdesign.googleio;

/**
 * Created by VCSDEV0100 on 4/27/2015.
 */

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class FadingActionBarHelper {
    private static final String TAG = "FadingActionBarHelper";
    private int mAlpha = 255;
    private Drawable mDrawable;
    private boolean isAlphaLocked;
    private final Toolbar mActionBar;
    public FadingActionBarHelper(final Toolbar actionBar) {
        mActionBar = actionBar;
    }
    public FadingActionBarHelper(final Toolbar actionBar, final Drawable drawable) {
        mActionBar = actionBar;
        setActionBarBackgroundDrawable(drawable);
    }
    public void setActionBarBackgroundDrawable(Drawable drawable) {
        setActionBarBackgroundDrawable(drawable, true);
    }
    public void setActionBarBackgroundDrawable(Drawable drawable, boolean mutate) {
        mDrawable = mutate ? drawable.mutate() : drawable;
        mActionBar.setBackgroundDrawable(mDrawable);
        if (mAlpha == 255) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                mAlpha = mDrawable.getAlpha();
        } else {
            setActionBarAlpha(mAlpha);
        }
    }
    /**
     * An {@link android.app.ActionBar} background drawable.
     *
     * @see #setActionBarBackgroundDrawable(android.graphics.drawable.Drawable)
     * @see #setActionBarAlpha(int)
     */
    public Drawable getActionBarBackgroundDrawable() {
        return mDrawable;
    }
    /**
     * Please use this method for global changes only!
     * This is helpful when you need to provide something like
     * Navigation drawer: lock ActionBar and set
     * {@link android.graphics.drawable.Drawable#setAlpha(int)}
     * to {@link #getActionBarBackgroundDrawable()} directly.
     *
     * @param alpha a value from 0 to 255
     * @see #getActionBarBackgroundDrawable()
     * @see #getActionBarAlpha()
     */
    public void setActionBarAlpha(int alpha) {
        if (mDrawable == null) {
            Log.e(TAG, "Set action bar background before setting the alpha level!");
            return;
        }
        if (!isAlphaLocked) mDrawable.setAlpha(alpha);
        mAlpha = alpha;
    }
    public int getActionBarAlpha() {
        return mAlpha;
    }
    /**
     * When ActionBar's alpha is locked {@link #setActionBarAlpha(int)}
     * won't change drawable\'s alpha (but will change {@link #getActionBarAlpha()} level)
     *
     * @param lock
     */
    public void setActionBarAlphaLocked(boolean lock) {
// Update alpha level on unlock
        if (isAlphaLocked != (isAlphaLocked = lock) && !isAlphaLocked) {
            setActionBarAlpha(mAlpha);
        }
    }
    public boolean isActionBarAlphaLocked() {
        return isAlphaLocked;
    }
}