package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.sdk.model.SearchResult;
import com.cncoderx.github.sdk.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface ISearchService {

    @GET("search/repositories")
    Call<SearchResult<Repository>> searchRepos(@Query("q") String key,
                                               @Query("sort") String sort,
                                               @Query("order") String order);

    @GET("search/users")
    Call<SearchResult<User>> searchUsers(@Query("q") String key,
                                         @Query("sort") String sort,
                                         @Query("order") String order);
}
