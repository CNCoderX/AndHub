package com.cncoderx.andhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.Issue;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IIssuesService;
import com.cncoderx.andhub.ui.activity.IssueCommentActivity;
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
public class RepoIssuesFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private String mOwner, mRepo;
    private BindingAdapter<Issue> mAdapter = new BindingAdapter<>(R.layout.item_issue_list, BR.issue);
    private Pagination<Issue> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Issue>() {
        IIssuesService mService = ServiceGenerator.create(IIssuesService.class);

        @Override
        public Single<List<Issue>> create(int pageIndex, int pageSize) {
            return mService.getIssues(mOwner, mRepo, pageIndex, pageSize);
        }
    });

    public static RepoIssuesFragment create(String owner, String repo) {
        RepoIssuesFragment fragment = new RepoIssuesFragment();
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
        setEmptyText(getString(R.string.no_issues));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<Issue>>bindToLifecycle());
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
        Issue issue = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), IssueCommentActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, mOwner);
        intent.putExtra(IntentExtra.KEY_REPO, mRepo);
        intent.putExtra(IntentExtra.KEY_NUMBER, issue.number);
        startActivity(intent);
    }
}
