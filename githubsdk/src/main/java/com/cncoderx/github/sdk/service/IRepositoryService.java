package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */

public interface IRepositoryService {
    @GET("repos/{owner}/{repo}")
    Call<Repository> getRepository(@Path("owner") String owner,
                                   @Path("repo") String repo);

    @GET("user/repos")
    Call<List<Repository>> getRepositories();

    @GET("users/{user}/repos")
    Call<List<Repository>> getRepositories(@Path("user") String user);

    @GET("user/starred")
    Call<List<Repository>> getStarredRepositories();

    @GET("users/{user}/starred")
    Call<List<Repository>> getStarredRepositories(@Path("user") String user);

}
