package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Comment;
import com.cncoderx.github.sdk.model.Issue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IIssuesService {

    @GET("repos/{owner}/{repo}/issues")
    Call<List<Issue>> getIssues(@Path("owner") String owner,
                                @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/issues/{number}")
    Call<Issue> getIssue(@Path("owner") String owner,
                         @Path("repo") String repo,
                         @Path("number") String number);

    @GET ("repos/{owner}/{repo}/issues/{number}/comments")
    Call<List<Comment>> getComments(@Path("owner") String owner,
                                    @Path("repo") String repo,
                                    @Path("number") String number);
}
