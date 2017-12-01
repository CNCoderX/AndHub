package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.sdk.model.SearchResult;
import com.cncoderx.github.sdk.service.ISearchService;
import com.cncoderx.github.ui.activity.RepositoryActivity;
import com.cncoderx.github.ui.adapter.RepoListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.SearchResultCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class SearchRepoFragment extends SwipeRecyclerViewFragment
        implements OnItemClickListener, OnLoadMoreListener {
    private RepoListAdapter mAdapter = new RepoListAdapter();
    private SearchResultCallback<Repository> mCallback = new SearchResultCallback<>(mAdapter);

    private String mKey, mSort, mOrder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
    }

    public void search(String key, String sort, String order, boolean refresh) {
        mKey = key; mSort = sort; mOrder = order;
        if (TextUtils.isEmpty(mKey)) {
            return;
        }
        setEmptyText(getString(R.string.search_no_result, key));
        ISearchService service = ServiceGenerator.create(ISearchService.class);
        Call<SearchResult<Repository>> call = service.searchRepos(key, sort, order);
        mCallback.setRefreshView(getSwipeRefreshLayout())
                .setEmptyView(getEmptyView())
                .queueTo(call, refresh);

        if (refresh) {
            getSwipeRefreshLayout().setRefreshing(true);
        }
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        mCallback.setLoadingView(view);
        search(mKey, mSort, mOrder, false);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), RepositoryActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        intent.putExtra(IntentExtra.KEY_REPO, repository.name);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (mAdapter.size() > 0)
                mAdapter.clear();
        }
    }
}
