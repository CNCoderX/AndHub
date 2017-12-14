package com.cncoderx.andhub.ui.activity;

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
public class RepoListActivity extends RecyclerViewActivity implements OnItemClickListener, OnLoadMoreListener {
    private BindingAdapter<Repository> mAdapter = new BindingAdapter<>(R.layout.item_repo_list, BR.repository);
    private Pagination<Repository> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Repository>() {
        IRepositoryService mService = ServiceGenerator.create(IRepositoryService.class);

        @Override
        public Single<List<Repository>> create(int pageIndex, int pageSize) {
            return mService.getRepositories(mUser, pageIndex, pageSize);
        }
    });
    private String mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_repos));
        mPagination.bind(getEmptyView());
        mPagination.bind(getRefreshLayout());
        mPagination.setTransformer(this.<List<Repository>>bindToLifecycle());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(this, RepositoryActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        intent.putExtra(IntentExtra.KEY_REPO, repository.name);
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

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        mPagination.bind(view);
        mPagination.nextPage();
    }
}
