package com.cncoderx.github.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.utils.BaseHandlerReference;

/**
 * @author cncoderx
 */
public class LinearGradientTextView extends TextView {
    private LinearGradient mLinearGradient;
    private Matrix mMatrix = new Matrix();
    private int mGradientWidth = 100;
    private int mGradientHeight = mGradientWidth / 4;
    private int[] mGradientColors = new int[3];
    private float[] mGradientPositions = {0, .5f, 1};
    private int mTranslationBegin;
    private int mTranslationEnd;
    private int mDuration = 3000;
    private float mStateDuration = 0f;
    private boolean isPlaying = false;

    private Handler mHandler = new ComputeTranslationHandler(this);

    static final int DELTA_TIME = 10;
    static final int MSG_COMPUTE_TRANS = 1;

    public LinearGradientTextView(Context context) {
        super(context);
        init();
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int textColor = getCurrentTextColor();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearGradientTextView);
        mDuration = a.getInteger(R.styleable.LinearGradientTextView_duration, mDuration);
        mGradientWidth = a.getDimensionPixelOffset(R.styleable.LinearGradientTextView_gradientWidth, (int) (getTextSize() * 3));
        mGradientHeight = mGradientWidth / 4;
        mGradientColors[0] = a.getColor(R.styleable.LinearGradientTextView_startColor, textColor);
        mGradientColors[1] = a.getColor(R.styleable.LinearGradientTextView_centerColor, 0xff000000);
        mGradientColors[2] = a.getColor(R.styleable.LinearGradientTextView_endColor, textColor);
        a.recycle();
        init();
    }

    private void init() {
        mLinearGradient = new LinearGradient(- mGradientWidth, - mGradientHeight, mGradientWidth, mGradientHeight,
                mGradientColors, mGradientPositions, Shader.TileMode.CLAMP);
        mLinearGradient.setLocalMatrix(mMatrix);
        getPaint().setShader(mLinearGradient);
    }

    public void setGradientTranslate(int translation) {
        mMatrix.setTranslate(translation, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        getPaint().setShader(mLinearGradient);
        invalidate();
    }

    public void start() {
        if (isPlaying()) {
            stop();
        }
        isPlaying = mHandler.sendEmptyMessage(MSG_COMPUTE_TRANS);
    }

    public void stop() {
        mHandler.removeMessages(MSG_COMPUTE_TRANS);
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTranslationBegin = - mGradientWidth * 2;
        mTranslationEnd = getMeasuredWidth() + mGradientWidth * 2;
        setGradientTranslate(mTranslationBegin);
    }

    void computeTranslation() {
        mStateDuration += DELTA_TIME;
        if (mStateDuration < mDuration) {
            float per = mStateDuration / mDuration;
            int translation = (int) (mTranslationBegin +
                    (mTranslationEnd - mTranslationBegin) * per);
            setGradientTranslate(translation);
        } else {
            mStateDuration = 0;
            setGradientTranslate(mTranslationBegin);
        }
        if (isPlaying()) {
            mHandler.sendEmptyMessageDelayed(MSG_COMPUTE_TRANS, DELTA_TIME);
        }
    }

    private static class ComputeTranslationHandler extends BaseHandlerReference<LinearGradientTextView> {

        public ComputeTranslationHandler(@NonNull LinearGradientTextView reference) {
            super(reference);
        }

        @Override
        public void handleMessage(LinearGradientTextView reference, Message msg) {
            if (msg.what == MSG_COMPUTE_TRANS) {
                reference.computeTranslation();
            }
        }
    }
}
