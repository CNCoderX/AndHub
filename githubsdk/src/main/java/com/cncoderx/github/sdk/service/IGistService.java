package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.model.Gist;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IGistService {

    @GET("gists/public")
    Call<List<Gist>> getPublicGists();

    @GET ("gists/{id}")
    Call<Gist> getGist(@Path("id") String id);

    @GET ("gists/{id}/star")
    Call<ResponseBody> isStarred(@Path("id") String id);

    @PUT("gists/{id}/star")
    Call<ResponseBody> star(@Path("id") String id);

    @DELETE("gists/{id}/star")
    Call<ResponseBody> unstar(@Path("id") String id);

    @GET("gists/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") String id);
}
