package com.cncoderx.github.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.adapter.ArrayAdapter;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

/**
 * @author cncoderx
 */
public class RepoCodePathFragment extends RecyclerViewFragment implements OnItemClickListener {
    private ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(R.layout.item_file_path) {
        @Override
        public void onBindViewHolder(BaseViewHolder holder, String string, int position) {
            super.onBindViewHolder(holder, string, position);
            if (position == size() - 1) {
                holder.getView(android.R.id.text1, TextView.class).setTextColor(
                        getResources().getColor(R.color.theme_blue));
            } else {
                holder.getView(android.R.id.text1, TextView.class).setTextColor(
                        getResources().getColor(R.color.white));
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setNotifyOnChange(false);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        addRootItem();
    }

    private void addRootItem() {
        String repo = getArguments().getString(IntentExtra.KEY_REPO);
        View root = LayoutInflater.from(getContext()).inflate(
                R.layout.item_file_path, getRecyclerView(), false);
        root.findViewById(android.R.id.icon).setVisibility(View.GONE);
        ((TextView) root.findViewById(android.R.id.text1)).setText(repo);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUpdateBroadcast("");
            }
        });
        RecyclerViewHelper.addHeaderView(getRecyclerView(), root);
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
        return null;
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        if (position == mAdapter.size() - 1)
            return;

        String[] tokens = new String[position + 1];
        for (int i = 0; i <= position; i++) {
            tokens[i] = mAdapter.get(i);
        }
        String path = TextUtils.join("/", tokens);
        sendUpdateBroadcast(path);
    }

    public void sendUpdateBroadcast(String path) {
        ((RepoCodeFragment) getParentFragment()).update(path);
//        Intent intent = new Intent(RepoDetailCodeFragment.ACTION);
//        intent.putExtra(IntentExtra.KEY_PATH, path);
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void update(String path) {
        mAdapter.clear();
//        mAdapter.add(mRoot);
        if (!TextUtils.isEmpty(path)) {
            String[] splited = path.trim().split("/");
            for (String str : splited) {
                if (!TextUtils.isEmpty(str)) {
                    mAdapter.add(str);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        int target = mAdapter.getItemCount();
        getRecyclerView().smoothScrollToPosition(target);
    }
}
