package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.MainActivityBinding;
import com.cncoderx.andhub.preference.ProfilePreference;
import com.cncoderx.andhub.ui.fragment.GistListFragment;
import com.cncoderx.andhub.ui.fragment.NewsListFragment;
import com.cncoderx.andhub.ui.fragment.OrganListFragment;
import com.cncoderx.andhub.ui.fragment.RepoListFragment;
import com.cncoderx.andhub.ui.fragment.StarListFragment;
import com.cncoderx.andhub.utils.IntentExtra;

public class MainActivity extends BaseActivity {
    private String mUser;
    private MainActivityBinding mBinding;
    private SparseArray<Fragment> mFragments = new SparseArray<>();

    public static final int FRAGMENT_INDEX_NEWS = 0;
    public static final int FRAGMENT_INDEX_REPOS = 1;
    public static final int FRAGMENT_INDEX_ORGANS = 2;
    public static final int FRAGMENT_INDEX_STARS = 3;
    public static final int FRAGMENT_INDEX_GISTS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mBinding.slidingPane.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelOpened(View panel) {
                mBinding.invalidateAll();
            }
        });

        ProfilePreference preference = new ProfilePreference(this);
        mUser = preference.getLogin();
        if (TextUtils.isEmpty(mUser)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }
        mBinding.setProfile(preference);
        mFragments.put(FRAGMENT_INDEX_NEWS, NewsListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_REPOS, RepoListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_ORGANS, OrganListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_STARS, StarListFragment.create(mUser));
        mFragments.put(FRAGMENT_INDEX_GISTS, GistListFragment.create(mUser));

        mBinding.setTitle(getString(R.string.news));
        switchFragment(FRAGMENT_INDEX_NEWS);
    }

    public void onMenu(View v) {
        if (mBinding.slidingPane.isOpen()) {
            mBinding.slidingPane.closePane();
        } else {
            mBinding.slidingPane.openPane();
        }
    }

    public void onSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        mBinding.slidingPane.closePane();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.slidingPane.isOpen()) {
            mBinding.slidingPane.closePane();
            return;
        }
        super.onBackPressed();
    }

    public void onFollowers(View v) {
        Intent intent = new Intent(this, FollowerListActivity.class);
        startActivity(intent);
        mBinding.slidingPane.closePane();
    }

    public void onFollowing(View v) {
        Intent intent = new Intent(this, FollowingListActivity.class);
        startActivity(intent);
        mBinding.slidingPane.closePane();
    }

    public void onNews(View v) {
        mBinding.slidingPane.closePane();
        mBinding.setTitle(getString(R.string.news));
        switchFragment(FRAGMENT_INDEX_NEWS);
    }

    public void onRepos(View v) {
        mBinding.slidingPane.closePane();
        mBinding.setTitle(getString(R.string.repositories));
        switchFragment(FRAGMENT_INDEX_REPOS);
    }

    public void onOrgans(View v) {
        mBinding.slidingPane.closePane();
        mBinding.setTitle(getString(R.string.organizations));
        switchFragment(FRAGMENT_INDEX_ORGANS);
    }

    public void onStars(View v) {
        mBinding.slidingPane.closePane();
        mBinding.setTitle(getString(R.string.stars));
        switchFragment(FRAGMENT_INDEX_STARS);
    }

    public void onGists(View v) {
        mBinding.slidingPane.closePane();
        mBinding.setTitle(getString(R.string.gists));
        switchFragment(FRAGMENT_INDEX_GISTS);
    }

    public void onSettings(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        mBinding.slidingPane.closePane();
    }

    public void onProfile(View v) {
        if (TextUtils.isEmpty(mUser)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(IntentExtra.KEY_USER, mUser);
            startActivity(intent);
            mBinding.slidingPane.closePane();
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
