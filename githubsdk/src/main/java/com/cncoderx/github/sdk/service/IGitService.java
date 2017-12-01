package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.GitTree;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IGitService {

    @GET("repos/{owner}/{repo}/git/refs/{branch}")
    Call<ResponseBody> getBranchReference(@Path("owner") String owner,
                                          @Path("repo") String repo,
                                          @Path("branch") String branch);

    @GET("repos/{owner}/{repo}/git/commits/{sha}")
    Call<ResponseBody> getGitCommit(@Path("owner") String owner,
                                    @Path("repo") String repo,
                                    @Path("sha") String sha);

    @GET("repos/{owner}/{repo}/git/trees/{sha}?recursive=1")
    Call<GitTree> getGitTree(@Path("owner") String owner,
                             @Path("repo") String repo,
                             @Path("sha") String sha);
}
