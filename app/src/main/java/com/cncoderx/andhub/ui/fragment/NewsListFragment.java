package com.cncoderx.andhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.Event;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IEventService;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class NewsListFragment extends RecyclerViewFragment implements OnLoadMoreListener {
    final Pagination.Factory<Event> mFactory = new Pagination.Factory<Event>() {
        @Override
        public Single<List<Event>> create(int pageIndex, int pageSize) {
            return ServiceGenerator.create(IEventService.class).getReceivedEvents(mUser, pageIndex, pageSize);
        }
    };
    private BindingAdapter<Event> mAdapter = new BindingAdapter<>(R.layout.item_news_list, BR.event);
    private Pagination<Event> mPagination = new Pagination<>(mAdapter, mFactory);
    private String mUser;

    public static NewsListFragment create(String user) {
        NewsListFragment fragment = new NewsListFragment();
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
        setOnLoadMoreListener(this);
        setEmptyText(getString(R.string.no_news));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<Event>>bindToLifecycle());
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
    protected boolean refreshable() {
        return true;
    }

    @Override
    public void onRefresh() {
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
    }
}
