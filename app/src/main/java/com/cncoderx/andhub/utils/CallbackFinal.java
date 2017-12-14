package com.cncoderx.andhub.utils;

import com.cncoderx.andhub.BuildConfig;

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
            onPostResponse(true);
        } else {
            onFailure(response.errorBody());
            onPostResponse(false);
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        onError(t);
        onPostResponse(false);
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

    public void onPostResponse(boolean successful) {

    }

    public void onError(Throwable t) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }
}
