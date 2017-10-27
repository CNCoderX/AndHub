package com.cncoderx.github.netservice;

import com.cncoderx.github.entites.Repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */

public interface IRepositoryService {
    @GET("repos/{owner}/{repo}")
    Call<Repository> getRepository(@Path("owner") String owner, @Path("repo") String repo);
}
