package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */

public interface IEventService {
    @GET("users/{user}/received_events")
    Call<List<Event>> getReceivedEvents(@Path("user") String user);
}
