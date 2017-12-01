package com.cncoderx.github.sdk.service;

import com.cncoderx.github.sdk.model.Profile;
import com.cncoderx.github.sdk.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author cncoderx
 */
public interface IUserService {
    @GET("user")
    Call<Profile> getProfile();

    @GET("users/{user}")
    Call<Profile> getProfile(@Path("user") String user);

    @GET("user/followers")
    Call<List<User>> getFollowers();

    @GET("users/{user}/followers")
    Call<List<User>> getFollowers(@Path("user") String user);

    @GET("user/following")
    Call<List<User>> getFollowing();

    @GET("users/{user}/following")
    Call<List<User>> getFollowing(@Path("user") String user);

    @GET("user/following/{user}")
    Call<ResponseBody> isFollowing(@Path("user") String user);

    @PUT("user/following/{user}")
    Call<ResponseBody> follow(@Path("user") String user);

    @DELETE("user/following/{user}")
    Call<ResponseBody> unfollow(@Path("user") String user);

    @GET("orgs/{org}")
    Call<Profile> getOrganProfile(@Path("org") String org);

    @GET("user/orgs")
    Call<List<User>> getOrgans();

    @GET("users/{user}/orgs")
    Call<List<User>> getOrgans(@Path("user") String user);

    @GET("orgs/{org}/members")
    Call<List<User>> getOrganMembers(@Path("org") String org);
}
