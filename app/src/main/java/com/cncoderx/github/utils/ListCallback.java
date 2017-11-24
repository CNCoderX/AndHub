package com.cncoderx.github.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.lang.reflect.Field;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author cncoderx
 */
public class ListCallback<T> implements Callback<List<T>> {
    private final ObjectAdapter<T> mAdapter;
    private boolean pagingLoad = true;
    private boolean pendingClear = false;

    private SwipeRefreshLayout refreshView;
    private ILoadingView loadingView;
    private View emptyView;

    private int mPage = FIRST_INDEX;
    private int mPageSize = DEFAULT_PAGE_SIZE;
    private int mDataSize = 0;

    public static final int FIRST_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 30;

    public ListCallback(@NonNull ObjectAdapter<T> adapter) {
        mAdapter = adapter;
    }

    public ListCallback(@NonNull ObjectAdapter<T> adapter, boolean pagingLoad) {
        mAdapter = adapter;
        this.pagingLoad = pagingLoad;
    }

    public void queueTo(Call<List<T>> call) {
        queueTo(call, false);
    }

    public void queueTo(Call<List<T>> call, boolean clear) {
        pendingClear = clear;
        if (clear)
            mPage = FIRST_INDEX;

        if (isPagingLoad()) {
            Request request = call.request();
            HttpUrl httpUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("page", Integer.toString(mPage))
                    .addQueryParameter("per_page", Integer.toString(mPageSize))
                    .build();
            try {
                Class clazz = request.getClass();
                Field field = clazz.getDeclaredField("url");
                field.setAccessible(true);
                field.set(request, httpUrl);
                field.setAccessible(false);
            } catch (Exception e) {

            }
        }
        call.enqueue(this);
    }

    public ListCallback<T> setRefreshView(SwipeRefreshLayout refreshView) {
        this.refreshView = refreshView;
        return this;
    }

    public ListCallback<T> setLoadingView(ILoadingView loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    public ListCallback<T> setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public boolean isPagingLoad() {
        return pagingLoad;
    }

    public void setPagingLoad(boolean pagingLoad) {
        this.pagingLoad = pagingLoad;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(@IntRange(from = FIRST_INDEX) int page) {
        mPage = page;
    }

    public void nextPage() {
        mPage++;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(@IntRange(from = 5) int pageSize) {
        mPageSize = pageSize;
    }

    @Override
    public void onResponse(Call<List<T>> call, Response<List<T>> response) {
        if (response.isSuccessful()) {
            if (pendingClear) {
                mAdapter.clear();
                pendingClear = false;
            }
            List<T> objects = response.body();
            if (objects != null && objects.size() > 0) {
                mAdapter.addAll(objects);
            }
            nextPage();
        }
        onPostResponse();
    }

//    private int queryPageIndex(Call<List<T>> call) {
//        HttpUrl httpUrl = call.request().url();
//        String page = httpUrl.queryParameter("page");
//        if (!TextUtils.isEmpty(page)) {
//            int pageIndex;
//            try {
//                pageIndex = Integer.parseInt(page);
//            } catch (NumberFormatException e) {
//                throw new AssertionError();
//            }
//            return pageIndex;
//        }
//        return -1;
//    }

    @Override
    public void onFailure(Call<List<T>> call, Throwable t) {
        onPostResponse();
    }

    public void onPostResponse() {
        int lastDataSize = mDataSize;
        mDataSize = mAdapter.size();

        if (loadingView != null) {
            if (mDataSize == lastDataSize) {
                loadingView.gone();
            } else {
                loadingView.hidden();
            }
        }
        if (emptyView != null) {
            if (mDataSize == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
        if (refreshView != null && refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
        }
    }
}
