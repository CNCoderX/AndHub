package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Event;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface IEventService {

    @GET("users/{user}/received_events")
    Single<List<Event>> getReceivedEvents(@Path("user") String user,
                                          @Query("page") int pageIndex,
                                          @Query("per_page") int pageSize);
}
