package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.ProfileActivityBinding;
import com.cncoderx.andhub.model.Profile;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IUserService;
import com.cncoderx.andhub.preference.ProfilePreference;
import com.cncoderx.andhub.utils.IntentExtra;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public class ProfileActivity extends BaseActivity {
    private ProfileActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String user = getIntent().getStringExtra(IntentExtra.KEY_USER);
        mBinding = DataBindingUtil.setContentView(this, R.layout.profile_activity);
        mBinding.setUser(user);
        newServiceCall();
    }

    private void newServiceCall() {
        String user = mBinding.getUser();
        if (!TextUtils.isEmpty(user)) {
            ServiceGenerator.create(IUserService.class)
                    .getUser(user)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.<Profile>bindToLifecycle())
                    .subscribe(new Consumer<Profile>() {
                        @Override
                        public void accept(Profile profile) throws Exception {
                            ProfilePreference preference = new ProfilePreference(ProfileActivity.this);
                            String login = preference.getLogin();
                            if (login.equals(profile.login)) {
                                preference.apply(profile);
                            }
                            mBinding.setProfile(profile);
                        }
                    });
        }
    }

    public void onFollowers(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, FollowerListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    public void onFollowing(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, FollowingListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    public void onOrgans(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, OrganListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    public void onMembers(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, MemberListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    public void onRepos(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, RepoListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    public void onGists(View v) {
        String user = mBinding.getUser();
        Intent intent = new Intent(this, GistListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }
}
