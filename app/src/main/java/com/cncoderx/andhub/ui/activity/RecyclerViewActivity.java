package com.cncoderx.andhub.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.andhub.R;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;

/**
 * @author cncoderx
 */
public class RecyclerViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mEmpty;
    private TextView mEmptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mEmpty = findViewById(R.id.empty);
        mEmptyText = (TextView) mEmpty.findViewById(R.id.tv_empty);

        mRefreshLayout.setEnabled(refreshable());
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getDefaultItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        RecyclerViewHelper.setOnItemClickListener(mRecyclerView, listener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        RecyclerViewHelper.setLoadMoreListener(
                mRecyclerView, R.layout.item_loading_more, listener);
    }

    @NonNull
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        Drawable drawable = getResources().getDrawable(R.drawable.list_divider);
        int size = getResources().getDimensionPixelOffset(R.dimen.divider_height);
        return new DividerItemDecoration(drawable, size);
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public View getEmptyView() {
        return mEmpty;
    }

    public void setEmptyText(String text) {
        mEmptyText.setText(text);
    }

    @LayoutRes
    protected int getContentLayout() {
        return R.layout.recycler_view_activity;
    }

    protected boolean refreshable() {
        return false;
    }

    @Override
    public void onRefresh() {

    }
}
