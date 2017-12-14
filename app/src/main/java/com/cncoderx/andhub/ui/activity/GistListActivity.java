package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.Gist;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IGistService;
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
public class GistListActivity extends RecyclerViewActivity implements OnItemClickListener, OnLoadMoreListener {
    private BindingAdapter<Gist> mAdapter = new BindingAdapter<>(R.layout.item_gist_list, BR.gist);
    private Pagination<Gist> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Gist>() {
        IGistService mService = ServiceGenerator.create(IGistService.class);

        @Override
        public Single<List<Gist>> create(int pageIndex, int pageSize) {
            return mService.getGists(mUser, pageIndex, pageSize);
        }
    });
    private String mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setOnLoadMoreListener(this);
        setEmptyText(getString(R.string.no_gists));
        mPagination.bind(getEmptyView());
        mPagination.bind(getRefreshLayout());
        mPagination.setTransformer(this.<List<Gist>>bindToLifecycle());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
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

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Gist gist = mAdapter.get(position);
        Intent intent = new Intent(this, GistActivity.class);
        intent.putExtra(IntentExtra.KEY_ID, gist.id);
        startActivity(intent);
    }

}
