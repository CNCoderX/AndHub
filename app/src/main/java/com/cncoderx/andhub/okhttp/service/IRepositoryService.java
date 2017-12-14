package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Repository;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */

public interface IRepositoryService {

    @GET("repos/{owner}/{repo}")
    Single<Repository> getRepository(@Path("owner") String owner,
                                     @Path("repo") String repo);

    @GET("user/repos")
    Single<List<Repository>> getRepositories(@Query("page") int pageIndex,
                                             @Query("per_page") int pageSize);

    @GET("users/{user}/repos")
    Single<List<Repository>> getRepositories(@Path("user") String user,
                                             @Query("page") int pageIndex,
                                             @Query("per_page") int pageSize);

    @GET("user/starred")
    Single<List<Repository>> getStarredRepositories(@Query("page") int pageIndex,
                                                    @Query("per_page") int pageSize);

    @GET("users/{user}/starred")
    Single<List<Repository>> getStarredRepositories(@Path("user") String user,
                                                    @Query("page") int pageIndex,
                                                    @Query("per_page") int pageSize);

}
