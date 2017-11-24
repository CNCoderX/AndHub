package com.cncoderx.github.sdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author cncoderx
 */
public class AuthorizationInterceptor implements Interceptor {
    private Context mContext;
    private static ArrayList<String> sAccessURLPath = new ArrayList<>();

    public AuthorizationInterceptor(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        if (isAccessRequest(request))
            return chain.proceed(request);

        final String token = TokenStore.getInstance(mContext).getToken();
        if (TextUtils.isEmpty(token)) {
            return chain.proceed(request);
        } else {
            Request interRequest = request.newBuilder()
                    .addHeader("Authorization", "token " + token)
                    .build();
            return chain.proceed(interRequest);
        }
    }

    public static boolean isAccessRequest(Request request) {
        String path = request.url().encodedPath();
        for (String s : sAccessURLPath) {
            if (path.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static void addAccessURLPath(String urlPath) {
        sAccessURLPath.add(urlPath);
    }

    public static void removeAccessURLPath(String urlPath) {
        sAccessURLPath.remove(urlPath);
    }

    public static void clearAccessURLPath() {
        sAccessURLPath.clear();
    }
}
