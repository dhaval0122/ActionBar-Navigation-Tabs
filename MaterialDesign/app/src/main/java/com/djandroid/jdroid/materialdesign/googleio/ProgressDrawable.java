package com.djandroid.jdroid.materialdesign.googleio;

/**
 * Created by dhawal sodha parmar on 5/2/2015.
 */


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class ProgressDrawable extends ColorDrawable {
    /**
     * The drawable's bounds Rect
     */
    private final Rect mBounds = getBounds();
    /**
     * Used to draw the progress bar
     */
    private final Paint mProgressPaint = new Paint();
    /**
     * The upper range of the progress bar
     */
    public int max = 100;
    /**
     * The current progress
     */
    public float progress;
    /**
     * Constructor for <code>ProgressDrawable</code>
     *
     * @param color The color to use
     */
    public ProgressDrawable(int color) {
        mProgressPaint.setColor(color);
        mProgressPaint.setAntiAlias(true);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
        final float top = mBounds.bottom * progress / max;
        canvas.drawRect(mBounds.left, top, mBounds.right, mBounds.bottom, mProgressPaint);
    }
    /**
     * Set the range of the progress bar, 0 - max
     *
     * @param max The upper range of this progress bar
     */
    public void setMax(int max) {
        this.max = max;
        invalidateSelf();
    }
    /**
     * Set the current progress to the specified value
     *
     * @param progress The new progress, between 0 and {@link #max}
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidateSelf();
    }
}