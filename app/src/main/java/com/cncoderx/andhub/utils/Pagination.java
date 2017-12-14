package com.cncoderx.andhub.utils;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.cncoderx.andhub.BuildConfig;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public class Pagination<T> {
    private int mPageIndex = FIRST_INDEX;
    private final int mPageSize;

    public static final int FIRST_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 30;

    private boolean isNewPage;
    private boolean isPagingEnd;

    private final BindingAdapter<T> mAdapter;
    private final Factory<T> mFactory;

    private ILoadingView mLoadingView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mEmptyView;

    private LifecycleTransformer<List<T>> mTransformer;

    public Pagination(@NonNull BindingAdapter<T> adapter, @NonNull Factory<T> factory) {
        this(adapter, factory, DEFAULT_PAGE_SIZE);
    }

    public Pagination(@NonNull BindingAdapter<T> adapter, @NonNull Factory<T> factory, int pageSize) {
        mAdapter = adapter;
        mFactory = factory;
        mPageSize = pageSize;
    }

    public void setTransformer(LifecycleTransformer<List<T>> transformer) {
        mTransformer = transformer;
    }

    public final void newPage() {
        isNewPage = true;
        mPageIndex = FIRST_INDEX;
        execute();
    }

    public final void nextPage() {
        isNewPage = false;
        execute();
    }

    private void execute() {
        Consumer<List<T>> success = new Consumer<List<T>>() {
            @Override
            public void accept(List<T> list) throws Exception {
                onSuccess(list);
                onComplete();
            }
        };
        Consumer<Throwable> error = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                onError(throwable);
                onComplete();
            }
        };
        if (mTransformer == null) {
            mFactory.create(mPageIndex, mPageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success, error);
        } else {
            mFactory.create(mPageIndex, mPageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(mTransformer)
                    .subscribe(success, error);
        }
    }

    protected void onSuccess(@NonNull List<T> list) {
        if (isNewPage) {
            mAdapter.clear();
        }
        if (list.size() > 0) {
            mAdapter.addAll(list);
            isPagingEnd = false;
        } else {
            isPagingEnd = true;
        }
        mPageIndex++;
    }

    protected void onError(Throwable throwable) {
        isPagingEnd = true;
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }

    protected void onComplete() {
        if (mLoadingView != null) {
            if (isPagingEnd) {
                mLoadingView.gone();
            } else {
                mLoadingView.hidden();
            }
        }
        if (mRefreshLayout != null &&
                mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (mEmptyView != null) {
            if (mAdapter.size() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }

    public void bind(ILoadingView loadingView) {
        mLoadingView = loadingView;
    }

    public void bind(SwipeRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

    public void bind(View emptyView) {
        mEmptyView = emptyView;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public boolean isPagingEnd() {
        return isPagingEnd;
    }

    public interface Factory<T> {
        Single<List<T>> create(int pageIndex, int pageSize);
    }
}
