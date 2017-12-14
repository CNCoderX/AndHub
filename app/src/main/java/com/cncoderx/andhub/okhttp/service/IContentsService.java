package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.File;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
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
    Single<ResponseBody> getReadme(@Path("owner") String owner,
                                   @Path("repo") String repo,
                                   @Query("ref") String ref);

    @GET("repos/{owner}/{repo}/contents/{path}")
    Single<List<File>> getContents(@Path("owner") String owner,
                                   @Path("repo") String repo,
                                   @Path("path") String path,
                                   @Query("ref") String ref,
                                   @Query("page") int pageIndex,
                                   @Query("per_page") int pageSize);
}
