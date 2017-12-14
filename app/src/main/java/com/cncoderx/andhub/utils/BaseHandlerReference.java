package com.cncoderx.andhub.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by wujie on 2016/6/27.
 */
public abstract class BaseHandlerReference<T> extends Handler {
    private final WeakReference<T> mReference;

    public BaseHandlerReference(@NonNull T reference) {
        mReference = new WeakReference<>(reference);
    }

    public BaseHandlerReference(@NonNull T reference, Callback callback) {
        super(callback);
        mReference = new WeakReference<>(reference);
    }

    public BaseHandlerReference(@NonNull T reference, Looper looper) {
        super(looper);
        mReference = new WeakReference<>(reference);
    }

    public BaseHandlerReference(@NonNull T reference, Looper looper, Callback callback) {
        super(looper, callback);
        mReference = new WeakReference<>(reference);
    }

    @Override
    public final void handleMessage(Message msg) {
        T reference = mReference.get();
        if (reference != null)
            handleMessage(reference, msg);
    }

    public abstract void handleMessage(T reference, Message msg);
}
