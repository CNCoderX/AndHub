package com.cncoderx.andhub.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cncoderx.andhub.R;

/**
 * @author cncoderx
 */
public class AnimatedImageView extends android.support.v7.widget.AppCompatImageView implements Handler.Callback {
    private int mFrameCount = 8;
    private int mFrameDuration = 80;
    private int mFrame = 0;
    private boolean mAuto = true;
    private boolean mLoop = true;
    private boolean isPlaying = false;

    private Handler mHandler = new Handler(this);

    static final int MSG_NEXT_FRAME = 1;
    static final int MIN_LEVEL = 0;
    static final int MAX_LEVEL = 10000;

    public AnimatedImageView(Context context) {
        super(context);
    }

    public AnimatedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedImageView);
        mFrameCount = a.getInt(R.styleable.AnimatedImageView_ivFrameCount, mFrameCount);
        mFrameDuration = a.getInt(R.styleable.AnimatedImageView_ivFrameDuration, mFrameDuration);
        mAuto = a.getBoolean(R.styleable.AnimatedImageView_ivIsAuto, mAuto);
        mLoop = a.getBoolean(R.styleable.AnimatedImageView_ivIsLoop, mLoop);
        a.recycle();
    }

    public void playAnimation() {
        if (mHandler.hasMessages(MSG_NEXT_FRAME))
            mHandler.removeMessages(MSG_NEXT_FRAME);

        isPlaying = true;
        nextFrame();
    }

    public void stopAnimation() {
        isPlaying = false;
        if (mHandler.hasMessages(MSG_NEXT_FRAME))
            mHandler.removeMessages(MSG_NEXT_FRAME);
    }

    public int getFrameCount() {
        return mFrameCount;
    }

    public void setFrameCount(int frameCount) {
        mFrameCount = frameCount;
    }

    public int getFrameDuration() {
        return mFrameDuration;
    }

    public void setFrameDuration(int frameDuration) {
        mFrameDuration = frameDuration;
    }

    public boolean isAuto() {
        return mAuto;
    }

    public void setAuto(boolean auto) {
        mAuto = auto;
    }

    public boolean isLoop() {
        return mLoop;
    }

    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAuto())
            playAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isPlaying)
            stopAnimation();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MSG_NEXT_FRAME) {
            nextFrame();
            return true;
        }
        return false;
    }

    private void nextFrame() {
        int frameCount = Math.abs(mFrameCount);
        if (frameCount <= 1) {
            stopAnimation();
            return;
        }
        int level = (MAX_LEVEL - MIN_LEVEL) * mFrame / (frameCount - 1);
        setImageLevel(level);
        if (mFrame < frameCount - 1) {
            mFrame++;
            mHandler.sendEmptyMessageDelayed(MSG_NEXT_FRAME, mFrameDuration);
        } else {
            if (isLoop()) {
                mFrame = 0;
                mHandler.sendEmptyMessageDelayed(MSG_NEXT_FRAME, mFrameDuration);
            } else {
                stopAnimation();
            }
        }
    }
}
