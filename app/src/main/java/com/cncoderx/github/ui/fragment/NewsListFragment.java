package com.cncoderx.github.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Event;
import com.cncoderx.github.sdk.service.IEventService;
import com.cncoderx.github.ui.adapter.NewsListAdapter;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class NewsListFragment extends SwipeRecyclerViewFragment implements OnLoadMoreListener {
    private NewsListAdapter mAdapter = new NewsListAdapter();
    private ListCallback<Event> mCallback = new ListCallback<>(mAdapter);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter);
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
        setEmptyText(getString(R.string.no_news));
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView loadingView) {
        IEventService service = ServiceGenerator.create(IEventService.class);
        Call<List<Event>> call = service.getReceivedEvents(AppContext.getInstance().getLoginName());
        mCallback.setLoadingView(loadingView).queueTo(call, false);
    }

    @Override
    public void onRefresh() {
        IEventService service = ServiceGenerator.create(IEventService.class);
        Call<List<Event>> call = service.getReceivedEvents(AppContext.getInstance().getLoginName());
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }
}
