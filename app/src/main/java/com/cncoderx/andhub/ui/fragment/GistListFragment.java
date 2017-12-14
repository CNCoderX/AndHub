package com.cncoderx.andhub.ui.fragment;

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
import com.cncoderx.andhub.ui.activity.GistActivity;
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
public class GistListFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private BindingAdapter<Gist> mAdapter = new BindingAdapter<>(R.layout.item_gist_list, BR.gist);
    private Pagination<Gist> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Gist>() {
        IGistService mService = ServiceGenerator.create(IGistService.class);

        @Override
        public Single<List<Gist>> create(int pageIndex, int pageSize) {
            return mService.getGists(mUser, pageIndex, pageSize);
        }
    });
    private String mUser;

    public static GistListFragment create(String user) {
        GistListFragment fragment = new GistListFragment();
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
        setEmptyText(getString(R.string.no_gists));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<Gist>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRefreshLayout().setRefreshing(true);
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
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
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        mPagination.bind(loadingView);
        mPagination.nextPage();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Gist gist = mAdapter.get(position);
        Intent intent = new Intent(getContext(), GistActivity.class);
        intent.putExtra(IntentExtra.KEY_ID, gist.id);
        startActivity(intent);
    }
}
