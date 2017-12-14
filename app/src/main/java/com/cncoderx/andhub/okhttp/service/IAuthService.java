package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Auth;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IAuthService {

    @GET("authorizations")
    Single<List<Auth>> getAuths();

    @GET("authorizations/{id}")
    Single<Auth> getAuth(@Path("id") String id);

    @POST("authorizations")
    Single<Auth> createAuth(@Header("Authorization") String authorization, @Body RequestBody body);

    @GET("applications/{client_id}/tokens/{token}")
    Completable checkAuth(@Header("Authorization") String authorization, @Path("client_id") String clientId, @Path("token") String token);

    @POST("applications/{client_id}/tokens/{token}")
    Completable resetAuth(@Header("Authorization") String authorization, @Path("client_id") String clientId, @Path("token") String token);

    @PATCH("authorizations/{id}")
    Completable updateAuth(@Path("id") String id);

    @DELETE("authorizations/{id}")
    Completable deleteAuth(@Path("id") String id);
}
