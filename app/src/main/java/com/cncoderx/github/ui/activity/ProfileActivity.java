package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Profile;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.switcher.ButtonSwitcher;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.switcher.FollowSwitcher;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.NumberFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class ProfileActivity extends BaseActivity {
    @BindView(R.id.iv_profile_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_profile_login)
    TextView tvLogin;

    @BindView(R.id.tv_profile_name)
    TextView tvName;

//    @BindView(R.id.tv_profile_bio)
//    TextView tvBio;

    @BindView(R.id.tv_profile_follower_count)
    TextView tvFollowers;

    @BindView(R.id.tv_profile_following_count)
    TextView tvFollowing;

    @BindView(R.id.tv_profile_follow)
    TextView tvFollow;

    @BindView(R.id.tv_profile_repos_count)
    TextView tvRepos;

    @BindView(R.id.tv_profile_gists_count)
    TextView tvGists;

    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        user = getIntent().getStringExtra(IntentExtra.KEY_USER);
        createFollowSwitcher();
        requestUIData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        user = intent.getStringExtra(IntentExtra.KEY_USER);
        createFollowSwitcher();
        requestUIData();
    }

    private void createFollowSwitcher() {
        FollowSwitcher switcher = new FollowSwitcher(tvFollow, user);
        switcher.setOnSwitchListener(new ButtonSwitcher.OnSwitchListener() {
            @Override
            public void switchOn(View button) {
                tvFollow.setText(R.string.unfollow);
            }

            @Override
            public void switchOff(View button) {
                tvFollow.setText(R.string.follow);
            }
        });
        switcher.begin();
    }

    private void requestUIData() {
        if (!TextUtils.isEmpty(user)) {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<Profile> call = service.getProfile(user);
            call.enqueue(new CallbackFinal<Profile>() {
                @Override
                public void onSuccess(Profile profile) {
                    if (profile != null)
                        updateUI(profile);
                }
            });
        }
    }

    private void updateUI(Profile profile) {
        Glide.with(this).load(profile.avatarUrl).into(ivAvatar);
        tvLogin.setText(profile.login);
        tvName.setText(profile.name);
//        if (TextUtils.isEmpty(profile.location)) {
//            tvLocal.setVisibility(View.GONE);
//        } else {
//            tvLocal.setVisibility(View.VISIBLE);
//            tvLocal.setText(profile.location);
//        }
//        tvBio.setText(profile.bio);
        tvFollowers.setText(NumberFormatter.format(profile.followers));
        tvFollowing.setText(NumberFormatter.format(profile.following));

        int reposCount = profile.publicRepos + profile.privateRepos;
        int gistsCount = profile.publicGists + profile.privateGists;
        if (reposCount == 0) {
            tvRepos.setVisibility(View.GONE);
        } else {
            tvRepos.setVisibility(View.VISIBLE);
            tvRepos.setText(NumberFormatter.format(reposCount));
        }
        if (gistsCount == 0) {
            tvGists.setVisibility(View.GONE);
        } else {
            tvGists.setVisibility(View.VISIBLE);
            tvGists.setText(NumberFormatter.format(gistsCount));
        }
    }

    @OnClick(R.id.ll_profile_follower)
    public void onFollowerClick(View v) {
        Intent intent = new Intent(this, FollowerListActivity.class);
        if (!TextUtils.isEmpty(user))
            intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_following)
    public void onFollowingClick(View v) {
        Intent intent = new Intent(this, FollowingListActivity.class);
        if (!TextUtils.isEmpty(user))
            intent.putExtra(IntentExtra.KEY_USER, user);
        startActivity(intent);
    }
}
