package com.cncoderx.andhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.PullRequest;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IPullsService;
import com.cncoderx.andhub.ui.activity.PullCommentActivity;
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
public class RepoPullsFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private String mOwner, mRepo;
    private BindingAdapter<PullRequest> mAdapter = new BindingAdapter<>(R.layout.item_pull_list, BR.pullRequest);
    private Pagination<PullRequest> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<PullRequest>() {
        IPullsService mService = ServiceGenerator.create(IPullsService.class);

        @Override
        public Single<List<PullRequest>> create(int pageIndex, int pageSize) {
            return mService.getPullRequests(mOwner, mRepo, pageIndex, pageSize);
        }
    });

    public static RepoPullsFragment create(String owner, String repo) {
        RepoPullsFragment fragment = new RepoPullsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_OWNER, owner);
        bundle.putString(IntentExtra.KEY_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOwner = getArguments().getString(IntentExtra.KEY_OWNER);
        mRepo = getArguments().getString(IntentExtra.KEY_REPO);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setOnLoadMoreListener(this);
        setEmptyText(getString(R.string.no_pulls));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<PullRequest>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        mPagination.bind(view);
        mPagination.nextPage();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        PullRequest pullRequest = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), PullCommentActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, mOwner);
        intent.putExtra(IntentExtra.KEY_REPO, mRepo);
        intent.putExtra(IntentExtra.KEY_NUMBER, pullRequest.number);
        startActivity(intent);
    }
}
