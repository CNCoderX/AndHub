package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.sdk.service.IRepositoryService;
import com.cncoderx.github.ui.activity.RepoDetailActivity;
import com.cncoderx.github.ui.adapter.RepoListAdapter;
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
public class StarListFragment extends SwipeRecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private RepoListAdapter mAdapter = new RepoListAdapter();
    private ListCallback<Repository> mCallback = new ListCallback<>(mAdapter);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
        setEmptyText(getString(R.string.no_stars));
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSwipeRefreshLayout().setRefreshing(true);
        onRefresh();
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        IRepositoryService service = ServiceGenerator.create(IRepositoryService.class);
        Call<List<Repository>> call = service.getStarredRepositories();
        mCallback.setLoadingView(loadingView).queueTo(call, false);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), RepoDetailActivity.class);
        intent.putExtra(IntentExtra.KEY_REPO, repository.fullName);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        intent.putExtra(IntentExtra.KEY_REPO, repository.name);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        IRepositoryService service = ServiceGenerator.create(IRepositoryService.class);
        Call<List<Repository>> call = service.getStarredRepositories();
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }
}