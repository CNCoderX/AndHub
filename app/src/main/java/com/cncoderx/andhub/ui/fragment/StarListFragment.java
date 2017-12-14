package com.cncoderx.andhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.Repository;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IRepositoryService;
import com.cncoderx.andhub.ui.activity.RepositoryActivity;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class StarListFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private BindingAdapter<Repository> mAdapter = new BindingAdapter<>(R.layout.item_repo_list, BR.repository);
    private Pagination<Repository> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Repository>() {
        IRepositoryService mService = ServiceGenerator.create(IRepositoryService.class);

        @Override
        public Single<List<Repository>> create(int pageIndex, int pageSize) {
            return mService.getStarredRepositories(mUser, pageIndex, pageSize);
        }
    });
    private String mUser;

    public static StarListFragment create(String user) {
        StarListFragment fragment = new StarListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_USER, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = getArguments().getString(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setOnLoadMoreListener(this);
        setEmptyText(getString(R.string.no_stars));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<Repository>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        mPagination.bind(loadingView);
        mPagination.nextPage();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), RepositoryActivity.class);
        intent.putExtra(IntentExtra.KEY_REPO, repository.fullName);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        startActivity(intent);
    }

    @Override
    protected boolean refreshable() {
        return true;
    }

    @Override
    public void onRefresh() {
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
    }
}
