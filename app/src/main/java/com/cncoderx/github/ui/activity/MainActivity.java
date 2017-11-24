package com.cncoderx.github.ui.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.TokenStore;
import com.cncoderx.github.sdk.model.Profile;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.ui.fragment.AboutFragment;
import com.cncoderx.github.ui.fragment.NewsListFragment;
import com.cncoderx.github.ui.fragment.RepoListFragment;
import com.cncoderx.github.ui.fragment.StarListFragment;
import com.cncoderx.github.ui.fragment.OrganListFragment;
import com.cncoderx.github.ui.fragment.GistListFragment;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.Constants;
import com.cncoderx.github.utils.NumberFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

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

    private SparseArray<Fragment> mFragments = new SparseArray<>();

    public static final int FRAGMENT_INDEX_NEWS = 0;
    public static final int FRAGMENT_INDEX_REPOS = 1;
    public static final int FRAGMENT_INDEX_ORGANS = 2;
    public static final int FRAGMENT_INDEX_STARS = 3;
    public static final int FRAGMENT_INDEX_GISTS = 4;
    public static final int FRAGMENT_INDEX_ABOUT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFragments.put(FRAGMENT_INDEX_NEWS, new NewsListFragment());
        mFragments.put(FRAGMENT_INDEX_REPOS, new RepoListFragment());
        mFragments.put(FRAGMENT_INDEX_ORGANS, new OrganListFragment());
        mFragments.put(FRAGMENT_INDEX_STARS, new StarListFragment());
        mFragments.put(FRAGMENT_INDEX_GISTS, new GistListFragment());
        mFragments.put(FRAGMENT_INDEX_ABOUT, new AboutFragment());

        IUserService service = ServiceGenerator.create(IUserService.class);
        Call<Profile> call = service.getProfile();
        call.enqueue(new CallbackFinal<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                if (profile != null)
                    updateUI(profile);
            }
        });

        switchFragment(FRAGMENT_INDEX_NEWS);
    }

    private void updateUI(Profile profile) {
        Glide.with(this).load(profile.avatarUrl).into(ivAvatar);
        tvLogin.setText(profile.login);
        tvName.setText(profile.name);
        tvFollowers.setText(NumberFormatter.format(profile.followers));
        tvFollowing.setText(NumberFormatter.format(profile.following));
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

    @OnClick(R.id.ll_main_menu_about)
    public void onAboutClick(View v) {
        mSlidingPaneLayout.closePane();
        switchFragment(FRAGMENT_INDEX_ABOUT);
    }

    @OnClick(R.id.fl_main_settings)
    public void onSettingsClick(View v) {
        mSlidingPaneLayout.closePane();
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fl_main_logout)
    public void onLogoutClick(View v) {
        mSlidingPaneLayout.closePane();
        new AlertDialog.Builder(this)
                .setMessage(R.string.logout_confirm_again)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void logout() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                am.removeAccount(account, this, null, null);
            } else {
                am.removeAccount(account, null, null);
            }
        }
        AppContext.getInstance().setLoginName(null);
        TokenStore.getInstance(getApplicationContext()).removeToken();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
