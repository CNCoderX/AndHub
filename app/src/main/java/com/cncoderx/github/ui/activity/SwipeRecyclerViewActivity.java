package com.cncoderx.github.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cncoderx.github.R;

import butterknife.BindView;

/**
 * @author cncoderx
 */
public class SwipeRecyclerViewActivity extends RecyclerViewActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_swipe_recycler_view;
    }

    @Override
    public void onRefresh() {

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
