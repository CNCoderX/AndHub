package com.cncoderx.github.ui.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cncoderx
 */
public class RecyclerViewActivity extends Activity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(android.R.id.empty)
    View mEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
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

    @NonNull
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        Drawable drawable = getResources().getDrawable(R.drawable.list_divider);
        int size = getResources().getDimensionPixelOffset(R.dimen.divider_height);
        return new DividerItemDecoration(drawable, size);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @LayoutRes
    protected int getContentLayout() {
        return R.layout.activity_recycler_view;
    }

    public View getEmptyView() {
        return mEmpty;
    }

    public void setEmptyText(CharSequence text) {
        TextView textView = (TextView) ((ViewGroup) mEmpty).getChildAt(0);
        textView.setText(text);
    }
}
