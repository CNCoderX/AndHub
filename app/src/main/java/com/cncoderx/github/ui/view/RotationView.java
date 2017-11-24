package com.cncoderx.github.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.cncoderx.github.R;

/**
 * @author cncoderx
 */
public class RotationView extends View implements Handler.Callback {
    private Drawable mIndeterminateDrawable;
    private int mFramesCount = 8;
    private int mFrameDuration = 80;
    private int mRotateDegree;

    private Handler mHandler = new Handler(this);

    static int MIN_DELTA_DEGREE = 0;

    public RotationView(Context context) {
        super(context);
    }

    public RotationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawable;
        int frameCount;
        int frameDuration;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotationView);
        drawable = a.getDrawable(R.styleable.RotationView_rvIndeterminateDrawable);
        frameCount = a.getInt(R.styleable.RotationView_rvFrameCount, mFramesCount);
        frameDuration = a.getInt(R.styleable.RotationView_rvFrameDuration, mFrameDuration);
        a.recycle();

        setIndeterminateDrawable(drawable);
        setFramesCount(frameCount);
        setFrameDuration(frameDuration);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        start();
    }

    public void start() {
        stop();
        invalidateDegree();
    }

    public void stop() {
        if (mHandler.hasMessages(1))
            mHandler.removeMessages(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        int w = 0, h = 0;
        float desiredAspect = 0f;
        if (mIndeterminateDrawable != null) {
            w = mIndeterminateDrawable.getIntrinsicWidth();
            h = mIndeterminateDrawable.getIntrinsicHeight();
            desiredAspect = (float) w / (float) h;
        }

        if (widthSpecMode == MeasureSpec.EXACTLY &&
                heightSpecMode == MeasureSpec.EXACTLY) {
            w += pLeft + pRight;
            h += pTop + pBottom;
            w = Math.max(w, getSuggestedMinimumWidth());
            h = Math.max(h, getSuggestedMinimumHeight());
            widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
            heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        } else {
            widthSize = resolveSizeAndState(w + pLeft + pRight, widthMeasureSpec, 0);
            heightSize = resolveSizeAndState(h + pTop + pBottom, heightMeasureSpec, 0);
            if (desiredAspect != 0.0f) {
                final float actualAspect = (float)(widthSize - pLeft - pRight) /
                        (heightSize - pTop - pBottom);

                if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {
                    boolean done = false;
                    if (widthSpecMode != MeasureSpec.EXACTLY) {
                        int newWidth = (int)(desiredAspect * (heightSize - pTop - pBottom)) + pLeft + pRight;
                        if (heightSpecMode == MeasureSpec.EXACTLY) {
                            widthSize = resolveSizeAndState(newWidth, widthMeasureSpec, 0);
                        }
                        if (newWidth <= widthSize) {
                            widthSize = newWidth;
                            done = true;
                        }
                    }
                    if (!done && heightSpecMode != MeasureSpec.EXACTLY) {
                        int newHeight = (int)((widthSize - pLeft - pRight) / desiredAspect) + pTop + pBottom;

                        if (widthSpecMode == MeasureSpec.EXACTLY) {
                            heightSize = resolveSizeAndState(newHeight, heightMeasureSpec, 0);
                        }

                        if (newHeight <= heightSize) {
                            heightSize = newHeight;
                        }
                    }
                }
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private void configureBounds() {
        if (mIndeterminateDrawable == null)
            return;

        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();

        mIndeterminateDrawable.setBounds(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIndeterminateDrawable == null)
            return;

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();
        int px = getWidth() + pLeft / 2 - pRight / 2;
        int py = getHeight() + pTop / 2 - pBottom / 2;

        canvas.save();
        canvas.rotate(mRotateDegree, px, py);
        if (pLeft != 0 || pTop != 0)
            canvas.translate(pLeft, pTop);

        mIndeterminateDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public Drawable getIndeterminateDrawable() {
        return mIndeterminateDrawable;
    }

    public void setIndeterminateDrawable(Drawable drawable) {
        mIndeterminateDrawable = drawable;
        configureBounds();
    }

    public int getFramesCount() {
        return mFramesCount;
    }

    public void setFramesCount(int framesCount) {
        mFramesCount = framesCount;
    }

    public int getFrameDuration() {
        return mFrameDuration;
    }

    public void setFrameDuration(int frameDuration) {
        mFrameDuration = frameDuration;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            invalidateDegree();
            return true;
        }
        return false;
    }

    private void invalidateDegree() {
        final int deltaDegree = 360 / mFramesCount;
        if (deltaDegree > MIN_DELTA_DEGREE) {
            mRotateDegree += deltaDegree;
            mRotateDegree %= 360;
            invalidate();
            mHandler.sendEmptyMessageDelayed(1, mFrameDuration);
        }
    }
}
