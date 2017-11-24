package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Auth;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */

public interface IAuthService {

    @POST("authorizations")
    Call<Auth> create(@Header("Authorization") String authorization, @Body RequestBody body);

    @GET("applications/{client_id}/tokens/{token}")
    Call<Auth> check(@Header("Authorization") String authorization, @Path("client_id") String clientId, @Path("token") String token);
}
