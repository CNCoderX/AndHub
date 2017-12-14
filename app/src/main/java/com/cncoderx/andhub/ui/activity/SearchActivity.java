package com.cncoderx.andhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.PopupWindowCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cncoderx.andhub.AppContext;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.SearchActivityBinding;
import com.cncoderx.andhub.ui.fragment.SearchRepoFragment;
import com.cncoderx.andhub.ui.fragment.SearchUserFragment;

public class SearchActivity extends BaseActivity
        implements TextView.OnEditorActionListener {

    private int mMenuTypeIndex = 0;
    private int mMenuSortIndex = 0;

    private SearchRepoFragment mRepoFragment;
    private SearchUserFragment mUserFragment;
    private Fragment currentFragment = null;

    private SearchActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.search_activity);
        mBinding.etSearchKey.setOnEditorActionListener(this);
        mRepoFragment = new SearchRepoFragment();
        mUserFragment = new SearchUserFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.container, mRepoFragment)
                .add(R.id.container, mUserFragment)
                .show(mRepoFragment)
                .hide(mUserFragment)
                .commit();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    public void onSearch(View v) {
        search();
        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void replaceFragment(Fragment show, Fragment hide) {
        currentFragment = show;
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .show(show).hide(hide)
                .commitAllowingStateLoss();
    }

    private void search() {
        String key = mBinding.etSearchKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            mBinding.etSearchKey.setText("");
            return;
        }

        String sort = null;
        String order = null;
        switch (mMenuSortIndex) {
            case 1:
                sort = "stars";
                order = "desc";
                break;
            case 2:
                sort = "forks";
                order = "desc";
                break;
            case 3:
                sort = "updated";
                order = "desc";
                break;
        }

        switch (mMenuTypeIndex) {
            case 0:
                if (currentFragment != mRepoFragment) {
                    replaceFragment(mRepoFragment, mUserFragment);
                }
                mRepoFragment.search(key, sort, order);
                break;
            case 1:
                if (currentFragment != mUserFragment) {
                    replaceFragment(mUserFragment, mRepoFragment);
                }
                mUserFragment.search(key, sort, order);
                break;
        }
    }

    public void onIcon(View v) {
        if (TextUtils.isEmpty(AppContext.getInstance().getToken())) {
            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void onMenu(View v) {
//        new SearchOptionsDialog().show(getFragmentManager(), "Options");
        new Menu(this).selectType(mMenuTypeIndex)
                .selectSort(mMenuSortIndex)
                .show(mBinding.llSearchActionBar, 0, 0, Gravity.RIGHT);
    }

    class Menu implements RadioGroup.OnCheckedChangeListener {
        PopupWindow mWindow;
        RadioGroup mGroup1, mGroup2;

        public Menu(Context context) {
            View v = LayoutInflater.from(context).inflate(R.layout.search_menu_layout, null);
            mGroup1 = (RadioGroup) v.findViewById(R.id.rg_search_menu_top);
            mGroup2 = (RadioGroup) v.findViewById(R.id.rg_search_menu_bottom);
            mWindow = new PopupWindow(v,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mWindow.setTouchable(true);
            mWindow.setOutsideTouchable(true);
            mWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
            mWindow.setAnimationStyle(R.style.popup_menu_anim);

            mGroup1.setOnCheckedChangeListener(this);
            mGroup2.setOnCheckedChangeListener(this);
        }

        public Menu selectType(int index) {
            mGroup1.setOnCheckedChangeListener(null);
            switch (index) {
                case 0:
                    mGroup1.check(R.id.tv_search_menu_repo);
                    break;
                case 1:
                    mGroup1.check(R.id.tv_search_menu_user);
                    break;
            }
            mGroup1.setOnCheckedChangeListener(this);
            return this;
        }

        public Menu selectSort(int index) {
            mGroup2.setOnCheckedChangeListener(null);
            switch (index) {
                case 0:
                    mGroup2.check(R.id.tv_search_menu_best_match);
                    break;
                case 1:
                    mGroup2.check(R.id.tv_search_menu_most_stars);
                    break;
                case 2:
                    mGroup2.check(R.id.tv_search_menu_most_forks);
                    break;
                case 3:
                    mGroup2.check(R.id.tv_search_menu_recently);
                    break;
            }
            mGroup2.setOnCheckedChangeListener(this);
            return this;
        }

        public void show(View anchor, int xoff, int yoff, int gravity) {
            if (!mWindow.isShowing()) {
                PopupWindowCompat.showAsDropDown(mWindow, anchor, xoff, yoff, gravity);
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (group == mGroup1) {
                switch (checkedId) {
                    case R.id.tv_search_menu_repo:
                        mMenuTypeIndex = 0;
                        mWindow.dismiss();
                        search();
                        break;
                    case R.id.tv_search_menu_user:
                        mMenuTypeIndex = 1;
                        mWindow.dismiss();
                        search();
                        break;
                }
            } else if (group == mGroup2) {
                switch (checkedId) {
                    case R.id.tv_search_menu_best_match:
                        mMenuSortIndex = 0;
                        mWindow.dismiss();
                        search();
                        break;
                    case R.id.tv_search_menu_most_stars:
                        mMenuSortIndex = 1;
                        mWindow.dismiss();
                        search();
                        break;
                    case R.id.tv_search_menu_most_forks:
                        mMenuSortIndex = 2;
                        mWindow.dismiss();
                        search();
                        break;
                    case R.id.tv_search_menu_recently:
                        mMenuSortIndex = 3;
                        mWindow.dismiss();
                        search();
                        break;
                }
            }
        }
    }
}
