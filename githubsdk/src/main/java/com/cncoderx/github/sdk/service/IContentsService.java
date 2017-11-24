package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Contents;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */

public interface IContentsService {

    @GET("repos/{owner}/{repo}/readme")
    @Headers("Accept: application/vnd.github.html")
    Call<ResponseBody> getReadme(@Path("owner") String owner,
                                 @Path("repo") String repo,
                                 @Query("ref") String ref);

    @GET("repos/{owner}/{repo}/contents/{path}")
    Call<List<Contents>> getContents(@Path("owner") String owner,
                                     @Path("repo") String repo,
                                     @Path("path") String path,
                                     @Query("ref") String ref);
}
