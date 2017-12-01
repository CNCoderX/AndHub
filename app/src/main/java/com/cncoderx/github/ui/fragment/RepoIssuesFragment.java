package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Issue;
import com.cncoderx.github.sdk.service.IIssuesService;
import com.cncoderx.github.ui.activity.IssueCommentActivity;
import com.cncoderx.github.ui.adapter.IssuesListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class RepoIssuesFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private String mOwner, mRepo;
    private IssuesListAdapter mAdapter = new IssuesListAdapter();
    private ListCallback<Issue> mCallback = new ListCallback<>(mAdapter);

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
        setEmptyText(getString(R.string.no_issues));
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IIssuesService service = ServiceGenerator.create(IIssuesService.class);
        Call<List<Issue>> call = service.getIssues(mOwner, mRepo);
        mCallback.queueTo(call, true);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        IIssuesService service = ServiceGenerator.create(IIssuesService.class);
        Call<List<Issue>> call = service.getIssues(mOwner, mRepo);
        mCallback.setLoadingView(view).queueTo(call, false);
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
