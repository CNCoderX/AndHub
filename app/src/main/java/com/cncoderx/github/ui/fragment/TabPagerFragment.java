package com.cncoderx.github.ui.fragment;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.github.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cncoderx
 */

public abstract class TabPagerFragment extends Fragment {
    @BindView(R.id.ll_tab_pager_bottom)
    LinearLayout mBottomBar;

    private View[] itemViews;
    private Fragment[] fragments;
    private int currentIndex = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_pager, container, false);
        ButterKnife.bind(this, view);
        init(inflater, view);
        return view;
    }

    private void init(LayoutInflater inflater, View view) {
        final int count = getCount();
        itemViews = new View[count];
        fragments = new Fragment[count];
        for (int index = 0; index < count; index++) {
            ViewGroup layout = createItemLayout(inflater, index);
            createNewItem(inflater, layout, index);
            mBottomBar.addView(layout, index);
            itemViews[index] = layout;
        }
    }

    ViewGroup createItemLayout(LayoutInflater inflater, final int index) {
        int hPadding = getResources().getDimensionPixelOffset(R.dimen.box_horizontal_padding);
        int vPadding = getResources().getDimensionPixelOffset(R.dimen.box_vertical_padding);
        FrameLayout layout = new FrameLayout(getActivity());
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
        Drawable drawable = getTabItemDrawable(index);
        View itemView = inflater.inflate(getTabItemLayout(), parent, false);
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        ImageView iconView = (ImageView) itemView.findViewById(android.R.id.icon);
        if (textView != null) {
            textView.setText(text);
            textView.setDuplicateParentStateEnabled(true);
        }
        if (iconView != null) {
            iconView.setImageDrawable(drawable);
            iconView.setDuplicateParentStateEnabled(true);
        }
        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) itemView.getLayoutParams();
        lParams.gravity = Gravity.CENTER;
        parent.addView(itemView, lParams);
        return itemView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        FragmentManager ft = getChildFragmentManager();
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

    public abstract Drawable getTabItemDrawable(int index);

    public abstract Fragment getFragment(int index);

    public int getCurrentIndex() {
        return currentIndex;
    }

    public @LayoutRes int getTabItemLayout() {
        return R.layout.tab_item_layout;
    }
}
