package com.cncoderx.github.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public class RecyclerViewFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(android.R.id.empty)
    View mEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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

    @NonNull
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        Drawable drawable = getResources().getDrawable(R.drawable.list_divider);
        int size = getResources().getDimensionPixelOffset(R.dimen.divider_height);
        return new DividerItemDecoration(drawable, size);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public View getEmptyView() {
        return mEmpty;
    }

    public void setEmptyText(CharSequence text) {
        TextView textView = (TextView) ((ViewGroup) mEmpty).getChildAt(0);
        textView.setText(text);
    }
}
