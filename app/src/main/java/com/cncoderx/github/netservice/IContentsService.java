package com.cncoderx.github.netservice;

import com.cncoderx.github.entites.Contents;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */

public interface IContentsService {
    @GET("repos/{owner}/{repo}/readme")
    Call<Contents> getReadme(@Path("owner") String owner, @Path("repo") String repo);
}
