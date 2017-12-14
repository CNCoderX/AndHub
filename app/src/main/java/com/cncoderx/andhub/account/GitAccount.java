package com.cncoderx.andhub.account;

import android.support.annotation.NonNull;

/**
 * @author cncoderx
 */
public class GitAccount implements Comparable<GitAccount> {
    String name;
    String password;
    String token;
    String avatar;
    long updated;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getUpdated() {
        return updated;
    }

    @Override
    public int compareTo(@NonNull GitAccount account) {
        return (int) (account.updated - updated);
    }
}
