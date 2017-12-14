package com.cncoderx.andhub.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.ui.view.IconView;

/**
 * @author cncoderx
 */
public abstract class TabPagerActivity extends BaseActivity {
    LinearLayout mBottomBar;

    private View[] itemViews;
    private Fragment[] fragments;
    private int currentIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pager_activity);
        mBottomBar = (LinearLayout) findViewById(R.id.ll_tab_pager_bottom);
        init();
    }

    private void init() {
        final int count = getCount();
        itemViews = new View[count];
        fragments = new Fragment[count];
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int index = 0; index < count; index++) {
            ViewGroup layout = createItemLayout(inflater, index);
            createNewItem(inflater, layout, index);
            mBottomBar.addView(layout, index);
            itemViews[index] = layout;
        }
    }

    ViewGroup createItemLayout(LayoutInflater inflater, final int index) {
        int hPadding = getResources().getDimensionPixelOffset(R.dimen.box_horizontal_padding);
        int vPadding = getResources().getDimensionPixelOffset(R.dimen.box_vertical_padding) / 2;
        FrameLayout layout = new FrameLayout(this);
        layout.setBackgroundColor(getResources().getColor(R.color.theme_black));
        layout.setPadding(hPadding, vPadding, hPadding, vPadding);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        layout.setLayoutParams(lParams);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyTabItemClicked(v, index);
            }
        });
        return layout;
    }

    View createNewItem(LayoutInflater inflater, ViewGroup parent, int index) {
        CharSequence text = getTabItemText(index);
        CharSequence unicode = getTabItemIconUnicode(index);
        View itemView = inflater.inflate(getTabItemLayout(), parent, false);
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        IconView iconView = (IconView) itemView.findViewById(android.R.id.icon);
        if (textView != null) {
            textView.setText(text);
            textView.setDuplicateParentStateEnabled(true);
        }
        if (iconView != null) {
            iconView.setText(unicode);
            iconView.setDuplicateParentStateEnabled(true);
        }
        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) itemView.getLayoutParams();
        lParams.gravity = Gravity.CENTER;
        parent.addView(itemView, lParams);
        return itemView;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        notifyTabItemClicked(null, 0);
    }

    private void notifyTabItemClicked(View v, int index) {
        if (currentIndex == index)
            return;

        int oldIndex = currentIndex;
        int newIndex = index;
        currentIndex = index;

        Fragment fragment = fragments[index];
        if (fragment == null) {
            fragment = fragments[index] = getFragment(index);
        }
        if (fragment == null) {
            throw new IllegalAccessError();
        }
        if (oldIndex >= 0) {
            itemViews[oldIndex].setSelected(false);
        }
        if (newIndex >= 0) {
            itemViews[newIndex].setSelected(true);
        }
        FragmentManager ft = getSupportFragmentManager();
        replaceFragment(ft, fragment);
        onTabSelectChanged(oldIndex, newIndex);
    }

    private void replaceFragment(FragmentManager fm, Fragment fragment) {
        String tag = null;
        if (currentIndex >= 0) {
            tag = getTabItemText(currentIndex).toString();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        ft.commit();
    }

    public void onTabSelectChanged(int oldIndex, int newIndex) {
        //
    }

    public abstract int getCount();

    public abstract CharSequence getTabItemText(int index);

    public abstract CharSequence getTabItemIconUnicode(int index);

    public abstract Fragment getFragment(int index);

    public int getCurrentIndex() {
        return currentIndex;
    }

    public @LayoutRes int getTabItemLayout() {
        return R.layout.tab_item_layout;
    }
}
