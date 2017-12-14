package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Comment;
import com.cncoderx.andhub.model.Gist;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface IGistService {

    @GET("gists")
    Single<List<Gist>> getGists(@Query("page") int pageIndex,
                                @Query("per_page") int pageSize);

    @GET("users/{user}/gists")
    Single<List<Gist>> getGists(@Path("user") String user,
                                @Query("page") int pageIndex,
                                @Query("per_page") int pageSize);

    @GET("gists/public")
    Single<List<Gist>> getPublicGists(@Query("page") int pageIndex,
                                      @Query("per_page") int pageSize);

    @GET ("gists/{id}")
    Single<Gist> getGist(@Path("id") String id);

    @GET ("gists/{id}/star")
    Completable isStarred(@Path("id") String id);

    @PUT("gists/{id}/star")
    Completable star(@Path("id") String id);

    @DELETE("gists/{id}/star")
    Completable unstar(@Path("id") String id);

    @GET("gists/{id}/comments")
    Single<List<Comment>> getComments(@Path("id") String id,
                                      @Query("page") int pageIndex,
                                      @Query("per_page") int pageSize);
}
