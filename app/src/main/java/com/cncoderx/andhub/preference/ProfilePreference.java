package com.cncoderx.andhub.preference;

import android.content.Context;

import com.cncoderx.andhub.model.Profile;

/**
 * @author cncoderx
 */
public class ProfilePreference extends BasePreference {
    private static final String PREF_NAME = "profile.pref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_BIO = "bio";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BLOG = "blog";
    private static final String KEY_FOLLOWERS = "followers";
    private static final String KEY_FOLLOWING = "following";

    public ProfilePreference(Context context) {
        super(context, PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getLogin() {
        return getString(KEY_LOGIN);
    }

    public ProfilePreference setLogin(String login) {
        putString(KEY_LOGIN, login);
        return this;
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public ProfilePreference setName(String name) {
        putString(KEY_NAME, name);
        return this;
    }

    public String getAvatar() {
        return getString(KEY_AVATAR);
    }

    public ProfilePreference setAvatar(String avatar) {
        putString(KEY_AVATAR, avatar);
        return this;
    }

    public String getBio() {
        return getString(KEY_BIO);
    }

    public ProfilePreference setBio(String bio) {
        putString(KEY_BIO, bio);
        return this;
    }

    public String getCompany() {
        return getString(KEY_COMPANY);
    }

    public ProfilePreference setCompany(String company) {
        putString(KEY_COMPANY, company);
        return this;
    }

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public ProfilePreference setLocation(String location) {
        putString(KEY_LOCATION, location);
        return this;
    }

    public String getEmail() {
        return getString(KEY_EMAIL);
    }

    public ProfilePreference setEmail(String email) {
        putString(KEY_EMAIL, email);
        return this;
    }

    public String getBlog() {
        return getString(KEY_BLOG);
    }

    public ProfilePreference setBlog(String blog) {
        putString(KEY_BLOG, blog);
        return this;
    }

    public int getFollowers() {
        return getInt(KEY_FOLLOWERS);
    }

    public ProfilePreference setFollowers(int followers) {
        putInt(KEY_FOLLOWERS, followers);
        return this;
    }

    public int getFollowing() {
        return getInt(KEY_FOLLOWING);
    }

    public ProfilePreference setFollowing(int following) {
        putInt(KEY_FOLLOWING, following);
        return this;
    }

    public final void apply(Profile profile) {
        setAvatar(profile.avatarUrl);
        setLogin(profile.login);
        setName(profile.name);
        setBio(profile.bio);
        setCompany(profile.company);
        setBlog(profile.blog);
        setEmail(profile.email);
        setLocation(profile.location);
        setFollowers(profile.followers);
        setFollowing(profile.following);
        apply();
    }
}
