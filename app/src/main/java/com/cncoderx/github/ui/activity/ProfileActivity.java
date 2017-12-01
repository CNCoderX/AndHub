package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Profile;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.switcher.ButtonSwitcher;
import com.cncoderx.github.switcher.FollowSwitcher;
import com.cncoderx.github.utils.CallbackFinal;
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

    @BindView(R.id.tv_profile_bio)
    TextView tvBio;

    @BindView(R.id.tv_profile_company)
    TextView tvCompany;

    @BindView(R.id.tv_profile_local)
    TextView tvLocal;

    @BindView(R.id.tv_profile_email)
    TextView tvEmail;

    @BindView(R.id.tv_profile_url)
    TextView tvUrl;

    @BindView(R.id.ll_profile_follow)
    LinearLayout llFollow;

    @BindView(R.id.tv_profile_follower_count)
    TextView tvFollowers;

    @BindView(R.id.tv_profile_following_count)
    TextView tvFollowing;

    @BindView(R.id.tv_profile_follow)
    TextView tvFollow;

    @BindView(R.id.ll_profile_member)
    LinearLayout llMember;

    @BindView(R.id.ll_profile_organ)
    LinearLayout llOrgan;

    @BindView(R.id.tv_profile_member_count)
    TextView tvMembers;

    @BindView(R.id.tv_profile_organ_count)
    TextView tvOrgans;

    @BindView(R.id.tv_profile_repos_count)
    TextView tvRepos;

    @BindView(R.id.tv_profile_gists_count)
    TextView tvGists;

    private String mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        requestUIData();
        createFollowSwitcher();
    }

    private void createFollowSwitcher() {
        FollowSwitcher switcher = new FollowSwitcher(tvFollow, mUser);
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
        if (!TextUtils.isEmpty(mUser)) {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<Profile> call = service.getProfile(mUser);
            call.enqueue(new CallbackFinal<Profile>() {
                @Override
                public void onSuccess(Profile profile) {
                    if (profile != null) {
                        updateUI(profile);
                    }
                }
            });
        }
    }

    private void updateUI(Profile profile) {
        if (profile.type == User.Type.Organization) {
            llMember.setVisibility(View.VISIBLE);
        } else if (profile.type == User.Type.User) {
            llOrgan.setVisibility(View.VISIBLE);
            llFollow.setVisibility(View.VISIBLE);
        }
        Glide.with(this).load(profile.avatarUrl).into(ivAvatar);
        tvLogin.setText(profile.login);
        tvName.setText(profile.name);
        tvBio.setText(profile.bio);
        if (!TextUtils.isEmpty(profile.company)) {
            ((ViewGroup) tvCompany.getParent()).setVisibility(View.VISIBLE);
            tvCompany.setText(profile.company);
        }
        if (!TextUtils.isEmpty(profile.location)) {
            ((ViewGroup) tvLocal.getParent()).setVisibility(View.VISIBLE);
            tvLocal.setText(profile.location);
        }
        if (!TextUtils.isEmpty(profile.email)) {
            ((ViewGroup) tvEmail.getParent()).setVisibility(View.VISIBLE);
            tvEmail.setText(profile.email);
        }
        if (!TextUtils.isEmpty(profile.blog)) {
            ((ViewGroup) tvUrl.getParent()).setVisibility(View.VISIBLE);
            tvUrl.setText(profile.blog);
        }
        tvFollowers.setText(NumberFormatter.format(profile.followers));
        tvFollowing.setText(NumberFormatter.format(profile.following));

//        int memberCount = profile.collaborators;
        int reposCount = profile.publicRepos + profile.privateRepos;
        int gistsCount = profile.publicGists + profile.privateGists;
//        if (memberCount > 0) {
//            tvMembers.setVisibility(View.VISIBLE);
//            tvMembers.setText(NumberFormatter.format(memberCount));
//        }
        if (reposCount > 0) {
            tvRepos.setVisibility(View.VISIBLE);
            tvRepos.setText(NumberFormatter.format(reposCount));
        }
        if (gistsCount > 0) {
            tvGists.setVisibility(View.VISIBLE);
            tvGists.setText(NumberFormatter.format(gistsCount));
        }
    }

    @OnClick(R.id.ll_profile_follower)
    public void onFollowerClick(View v) {
        Intent intent = new Intent(this, FollowerListActivity.class);
        if (!TextUtils.isEmpty(mUser))
            intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_following)
    public void onFollowingClick(View v) {
        Intent intent = new Intent(this, FollowingListActivity.class);
        if (!TextUtils.isEmpty(mUser))
            intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_organ)
    public void onOrganClick(View v) {
        Intent intent = new Intent(this, OrganListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_member)
    public void onMemberClick(View v) {
        Intent intent = new Intent(this, MemberListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_repos)
    public void onReposClick(View v) {
        Intent intent = new Intent(this, RepoListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }

    @OnClick(R.id.ll_profile_gists)
    public void onGistsClick(View v) {
        Intent intent = new Intent(this, GistListActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, mUser);
        startActivity(intent);
    }
}
