package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.service.IGistService;
import com.cncoderx.github.sdk.model.Gist;
import com.cncoderx.github.ui.activity.GistActivity;
import com.cncoderx.github.ui.adapter.GistListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class GistListFragment extends SwipeRecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private GistListAdapter mAdapter = new GistListAdapter();
    private ListCallback<Gist> mCallback = new ListCallback<>(mAdapter);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
        setEmptyText(getString(R.string.no_gists));
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSwipeRefreshLayout().setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        IGistService service = ServiceGenerator.create(IGistService.class);
        Call<List<Gist>> call = service.getPublicGists();
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        IGistService service = ServiceGenerator.create(IGistService.class);
        Call<List<Gist>> call = service.getPublicGists();
        mCallback.setLoadingView(loadingView).queueTo(call, false);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Gist gist = mAdapter.get(position);
        Intent intent = new Intent(getContext(), GistActivity.class);
        intent.putExtra(IntentExtra.KEY_ID, gist.id);
        startActivity(intent);
    }
}
