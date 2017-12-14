package com.cncoderx.andhub.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cncoderx.andhub.R;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;

/**
 * @author cncoderx
 */
public class RecyclerViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mEmpty;
    private TextView mEmptyText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getViewLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mEmpty = view.findViewById(R.id.empty);
        mEmptyText = (TextView) mEmpty.findViewById(R.id.tv_empty);

        mRefreshLayout.setEnabled(refreshable());
        mRefreshLayout.setOnRefreshListener(this);

        Context context = getContext();
        mRecyclerView.setLayoutManager(getLayoutManager(context));
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
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
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
    protected int getViewLayout() {
        return R.layout.recycler_view_fragment;
    }

    protected boolean refreshable() {
        return false;
    }

    @Override
    public void onRefresh() {

    }

}
