package com.cncoderx.andhub.okhttp;

import com.cncoderx.andhub.BuildConfig;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author cncoderx
 */
public abstract class Subscriber<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }
}
