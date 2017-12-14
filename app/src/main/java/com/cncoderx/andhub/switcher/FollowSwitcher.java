package com.cncoderx.andhub.switcher;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IUserService;

import io.reactivex.Completable;

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
    public Completable isSelectedCall() {
        return mService.isFollowing(user);
    }

    @NonNull
    @Override
    public Completable getSelectCall() {
        return mService.follow(user);
    }

    @NonNull
    @Override
    public Completable getUnselectCall() {
        return mService.unfollow(user);
    }

    @BindingAdapter({"follow_switcher_user", "follow_switcher_on", "follow_switcher_off"})
    public static void setFollowSwitcher(final TextView view, String user, final String on, final String off) {
        FollowSwitcher switcher = new FollowSwitcher(view, user);
        switcher.setOnSwitchListener(new ButtonSwitcher.OnSwitchListener() {
            @Override
            public void switchOn(View button) {
                view.setText(on);
            }

            @Override
            public void switchOff(View button) {
                view.setText(off);
            }
        });
        switcher.begin();
    }
}
