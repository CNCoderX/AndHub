package com.cncoderx.github.utils;

import com.cncoderx.github.BuildConfig;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author cncoderx
 */

public abstract class CallbackFinal<T> implements Callback<T> {

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onFailure(response.errorBody());
        }
        onPostResponse();
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        onError(t);
        onPostResponse();
    }

    public abstract void onSuccess(T t);

    public void onFailure(ResponseBody body) {
        if (BuildConfig.DEBUG) {
            try {
                String msg = body.string();
                if (msg != null)
                    System.out.println("CallbackFinal-onFailure:" + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPostResponse() {

    }

    public void onError(Throwable t) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }
}
