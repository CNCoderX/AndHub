package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.model.PullRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IPullsService {

    @GET("repos/{owner}/{repo}/pulls")
    Call<List<PullRequest>> getPullRequests(@Path("owner") String owner,
                                            @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/pulls/{number}")
    Call<PullRequest> getPullRequest(@Path("owner") String owner,
                                     @Path("repo") String repo,
                                     @Path("number") String number);

    @GET ("repos/{owner}/{repo}/pulls/{number}/comments")
    Call<List<Comment>> getComments(@Path("owner") String owner,
                                    @Path("repo") String repo,
                                    @Path("number") String number);
}
