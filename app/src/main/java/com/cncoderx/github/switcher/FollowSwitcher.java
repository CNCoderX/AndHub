package com.cncoderx.github.switcher;

import android.support.annotation.NonNull;
import android.view.View;

import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.service.IUserService;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class FollowSwitcher extends ButtonSwitcher {
    private final String user;
    private IUserService mService;

    public FollowSwitcher(@NonNull View button, String user) {
        super(button);
        this.user = user;
        mService = ServiceGenerator.create(IUserService.class);
    }

    @NonNull
    @Override
    public Call<ResponseBody> isSelectedCall() {
        return mService.isFollowing(user);
    }

    @NonNull
    @Override
    public Call<ResponseBody> getSelectCall() {
        return mService.follow(user);
    }

    @NonNull
    @Override
    public Call<ResponseBody> getUnselectCall() {
        return mService.unfollow(user);
    }
}
