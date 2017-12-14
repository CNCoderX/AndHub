package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Comment;
import com.cncoderx.andhub.model.PullRequest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface IPullsService {

    @GET("repos/{owner}/{repo}/pulls")
    Single<List<PullRequest>> getPullRequests(@Path("owner") String owner,
                                            @Path("repo") String repo,
                                            @Query("page") int pageIndex,
                                            @Query("per_page") int pageSize);

    @GET("repos/{owner}/{repo}/pulls/{number}")
    Single<PullRequest> getPullRequest(@Path("owner") String owner,
                                       @Path("repo") String repo,
                                       @Path("number") String number);

    @GET ("repos/{owner}/{repo}/pulls/{number}/comments")
    Single<List<Comment>> getComments(@Path("owner") String owner,
                                    @Path("repo") String repo,
                                    @Path("number") String number,
                                    @Query("page") int pageIndex,
                                    @Query("per_page") int pageSize);
}
