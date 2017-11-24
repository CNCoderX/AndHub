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
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.NumberFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class OrganizationActivity extends BaseActivity {
    @BindView(R.id.iv_organ_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_organ_login)
    TextView tvLogin;

    @BindView(R.id.tv_organ_name)
    TextView tvName;

//    @BindView(R.id.tv_organ_desc)
//    TextView tvDesc;

    @BindView(R.id.tv_organ_member_count)
    TextView tvMembers;

    @BindView(R.id.tv_organ_repos_count)
    TextView tvRepos;

    @BindView(R.id.tv_organ_gists_count)
    TextView tvGists;

    private String organ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        ButterKnife.bind(this);

        organ = getIntent().getStringExtra(IntentExtra.KEY_ORGAN);
        requestUIData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        organ = intent.getStringExtra(IntentExtra.KEY_ORGAN);
        requestUIData();
    }

    private void requestUIData() {
        if (!TextUtils.isEmpty(organ)) {
            IUserService service = ServiceGenerator.create(IUserService.class);
            Call<Profile> call = service.getOrganProfile(organ);
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
//        tvFollowers.setText(NumberFormatter.format(profile.followers));
//        tvFollowing.setText(NumberFormatter.format(profile.following));

        int memberCount = profile.collaborators;
        int reposCount = profile.publicRepos + profile.privateRepos;
        int gistsCount = profile.publicGists + profile.privateGists;
        if (memberCount == 0) {
            tvMembers.setVisibility(View.GONE);
        } else {
            tvMembers.setVisibility(View.VISIBLE);
            tvMembers.setText(NumberFormatter.format(memberCount));
        }
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
}
