package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Repository;
import com.cncoderx.andhub.model.SearchResult;
import com.cncoderx.andhub.model.User;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface ISearchService {

    @GET("search/repositories")
    Single<SearchResult<Repository>> searchRepos(@Query("q") String key,
                                                 @Query("sort") String sort,
                                                 @Query("order") String order,
                                                 @Query("page") int pageIndex,
                                                 @Query("per_page") int pageSize);

    @GET("search/users")
    Single<SearchResult<User>> searchUsers(@Query("q") String key,
                                           @Query("sort") String sort,
                                           @Query("order") String order,
                                           @Query("page") int pageIndex,
                                           @Query("per_page") int pageSize);
}
