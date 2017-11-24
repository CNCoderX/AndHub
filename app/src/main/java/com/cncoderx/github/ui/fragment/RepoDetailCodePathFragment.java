package com.cncoderx.github.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.cncoderx.github.R;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.recyclerviewhelper.adapter.ArrayAdapter;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

/**
 * @author cncoderx
 */
public class RepoDetailCodePathFragment extends RecyclerViewFragment implements OnItemClickListener {
    private ArrayAdapter<String> mAdapter = new ArrayAdapter<>(R.layout.item_file_path);
    private String mRoot;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRoot = getArguments().getString(IntentExtra.KEY_REPO);
        mAdapter.setNotifyOnChange(false);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update("");
    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.div_arrow);
//        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL);
        itemDecoration.setDrawable(drawable);
        return itemDecoration;
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        if (position == 0) {
            sendUpdateBroadcast("");
        } else if (position < mAdapter.getItemCount() - 1){
            String[] tokens = new String[position];
            for (int i = 1; i <= position; i++) {
                tokens[i - 1] = mAdapter.get(i);
            }
            String path = TextUtils.join("/", tokens);
            sendUpdateBroadcast(path);
        }
    }

    public void sendUpdateBroadcast(String path) {
        ((RepoDetailCodeFragment) getParentFragment()).update(path);
//        Intent intent = new Intent(RepoDetailCodeFragment.ACTION);
//        intent.putExtra(IntentExtra.KEY_PATH, path);
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void update(String path) {
        mAdapter.clear();
        mAdapter.add(mRoot);
        if (!TextUtils.isEmpty(path)) {
            String[] splited = path.trim().split("/");
            for (String str : splited) {
                if (!TextUtils.isEmpty(str)) {
                    mAdapter.add(str);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        getRecyclerView().smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }
}
