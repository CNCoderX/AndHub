package com.cncoderx.andhub.okhttp;

import android.content.Context;

import com.cncoderx.andhub.BuildConfig;
import com.cncoderx.andhub.okhttp.interceptor.AuthorizationInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author cncoderx
 */
public class ServiceGenerator {
    private static boolean initialized;
    private static Retrofit retrofit;

    public static void initialize(Context context, HttpConfig config) {
        retrofit = new Retrofit.Builder()
                .baseUrl(config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(buildHttpClient(context, config))
                .build();
        initialized = true;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <T> T create(Class<T> service) {
        if (!initialized) {
            throw new IllegalAccessError("Service generator not initialized");
        }
        return retrofit.create(service);
    }

    private static OkHttpClient buildHttpClient(Context context, HttpConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AuthorizationInterceptor(context));
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS);
        builder.readTimeout(config.getReadTimeout(), TimeUnit.SECONDS);
        return builder.build();
    }

}
