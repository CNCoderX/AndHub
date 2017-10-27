package com.cncoderx.github.netservice;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author cncoderx
 */

public interface ILoginService {
//    @POST("authorizations")
//    @FormUrlEncoded
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    Call<ResponseBody> login(@Header("Authorization") String authorization,
//                             @Field("client_id") String clientId,
//                             @Field("client_secret") String clientSecret,
//                             @Field("note") String note,
//                             @Field("note_url") String noteUrl,
//                             @Field("fingerprint") String fingerprint,
//                             @Field("scopes") String scopes);

    @POST("authorizations")
    Call<ResponseBody> login(@Header("Authorization") String authorization, @Body RequestBody body);
}
