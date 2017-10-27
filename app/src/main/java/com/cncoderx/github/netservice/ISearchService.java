package com.cncoderx.github.netservice;

import com.cncoderx.github.entites.Repositories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface ISearchService {

    @GET("search/repositories")
    Call<Repositories> search(@Query("q") String key,
                              @Query("sort") String sort,
                              @Query("order") String order,
                              @Query("page") int page,
                              @Query("per_page") int pageSize);
}
