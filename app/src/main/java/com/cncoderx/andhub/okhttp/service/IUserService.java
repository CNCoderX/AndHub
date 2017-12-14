package com.cncoderx.andhub.okhttp.service;

import com.cncoderx.andhub.model.Profile;
import com.cncoderx.andhub.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author cncoderx
 */
public interface IUserService {

    @GET("user")
    Single<Profile> getUser();

    @GET("users/{user}")
    Single<Profile> getUser(@Path("user") String user);

    @GET("user/followers")
    Single<List<User>> getFollowers(@Query("page") int pageIndex,
                                    @Query("per_page") int pageSize);

    @GET("users/{user}/followers")
    Single<List<User>> getFollowers(@Path("user") String user,
                                    @Query("page") int pageIndex,
                                    @Query("per_page") int pageSize);

    @GET("user/following")
    Single<List<User>> getFollowing(@Query("page") int pageIndex,
                                    @Query("per_page") int pageSize);

    @GET("users/{user}/following")
    Single<List<User>> getFollowing(@Path("user") String user,
                                    @Query("page") int pageIndex,
                                    @Query("per_page") int pageSize);

    @GET("user/following/{user}")
    Completable isFollowing(@Path("user") String user);

    @PUT("user/following/{user}")
    Completable follow(@Path("user") String user);

    @DELETE("user/following/{user}")
    Completable unfollow(@Path("user") String user);

    @GET("users/{user}/orgs")
    Single<List<User>> getOrgans(@Path("user") String user,
                                 @Query("page") int pageIndex,
                                 @Query("per_page") int pageSize);

    @GET("orgs/{org}/members")
    Single<List<User>> getOrganMembers(@Path("org") String org,
                                       @Query("page") int pageIndex,
                                       @Query("per_page") int pageSize);
}
