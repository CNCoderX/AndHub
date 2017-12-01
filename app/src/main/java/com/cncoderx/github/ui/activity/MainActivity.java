package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.R;
import com.cncoderx.github.preference.ProfilePreference;
import com.cncoderx.github.ui.fragment.GistListFragment;
import com.cncoderx.github.ui.fragment.NewsListFragment;
import com.cncoderx.github.ui.fragment.OrganListFragment;
import com.cncoderx.github.ui.fragment.RepoListFragment;
import com.cncoderx.github.ui.fragment.StarListFragment;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.NumberFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.sliding_pane_layout)
    SlidingPaneLayout mSlidingPaneLayout;

    @BindView(R.id.iv_main_user_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_main_user_login)
    TextView tvLogin;

    @BindView(R.id.tv_main_user_name)
    TextView tvName;

    @BindView(R.id.tv_main_follower_count)
    TextView tvFollowers;

    @BindView(R.id.tv_main_following_count)
    TextView tvFollowing;

    private String mUser;

    private SparseArray<Fragment> mFragments = new SparseArray<>();

    public static final int FRAGMENT_INDEX_NEWS = 0;
    public static final int FRAGMENT_INDEX_REPOS = 1;
    public static final int FRAGMENT_INDEX_ORGANS = 2;
    public static final int FRAGMENT_INDEX_STARS = 3;
    public static final int FRAGMENT_INDEX_GISTS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ProfilePreference preference = new ProfilePreference(this);
        mUser = preference.getLogin();
        if (TextUtils.isEmpty(mUser)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        mFragments.put(FRAGMENT_INDEX_NEWS, NewsListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_REPOS, RepoListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_ORGANS, OrganListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_STARS, StarListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_GISTS, GistListFragment.create(mUser));
        switchFragment(FRAGMENT_INDEX_NEWS);

        Glide.with(this).load(preference.getAvatar()).into(ivAvatar);
        tvLogin.setText(preference.getLogin());
        tvName.setText(preference.getName());
        tvFollowers.setText(NumberFormatter.format(preference.getFollowers()));
        tvFollowing.setText(NumberFormatter.format(preference.getFollowing()));
    }

    @OnClick(R.id.iv_main_menu)
    public void onMenuClick(View v) {
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        } else {
            mSlidingPaneLayout.openPane();
        }
    }

    @OnClick(R.id.iv_main_search)
    public void onSearchClick(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
            return;
        }
        super.onBackPressed();
    }

    @OnClick(R.id.ll_main_follower_count)
    public void onFollowersClick(View v) {
        mSlidingPaneLayout.closePane();
        Intent intent = new Intent(this, FollowerListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_main_following_count)
    public void onFollowingClick(View v) {
        mSlidingPaneLayout.closePane();
        Intent intent = new Intent(this, FollowingListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_main_menu_news)
    public void onNewsClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_NEWS);
    }

    @OnClick(R.id.ll_main_menu_repos)
    public void onReposClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_REPOS);
    }

    @OnClick(R.id.ll_main_menu_organs)
    public void onOrgansClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_ORGANS);
    }

    @OnClick(R.id.ll_main_menu_stars)
    public void onStarsClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_STARS);
    }

    @OnClick(R.id.ll_main_menu_gists)
    public void onGistsClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_GISTS);
    }

    @OnClick(R.id.ll_main_menu_setting)
    public void onSettingsClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_main_profile)
    public void onProfileClick(View v) {
        if (TextUtils.isEmpty(mUser)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(IntentExtra.KEY_USER, mUser);
            startActivity(intent);
        }
    }

    private void switchFragment(int index) {
        Fragment fragment = mFragments.get(index);
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }
    }
}
