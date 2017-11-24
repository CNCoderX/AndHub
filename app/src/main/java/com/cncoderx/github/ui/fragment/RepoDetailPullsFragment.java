package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.PullRequest;
import com.cncoderx.github.sdk.service.IPullsService;
import com.cncoderx.github.ui.activity.PullCommentActivity;
import com.cncoderx.github.ui.adapter.PullsListAdapter;
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
public class RepoDetailPullsFragment extends RecyclerViewFragment implements OnItemClickListener, OnLoadMoreListener {
    private String mOwner, mRepo;
    private PullsListAdapter mAdapter = new PullsListAdapter();
    private ListCallback<PullRequest> mCallback = new ListCallback<>(mAdapter);

    public static RepoDetailPullsFragment create(String owner, String repo) {
        RepoDetailPullsFragment fragment = new RepoDetailPullsFragment();
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
        setEmptyText(getString(R.string.no_pulls));
        RecyclerViewHelper.setLoadMoreListener(
                getRecyclerView(), R.layout.item_loading_more, this);
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IPullsService service = ServiceGenerator.create(IPullsService.class);
        Call<List<PullRequest>> call = service.getPullsList(mOwner, mRepo);
        mCallback.queueTo(call, true);
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        IPullsService service = ServiceGenerator.create(IPullsService.class);
        Call<List<PullRequest>> call = service.getPullsList(mOwner, mRepo);
        mCallback.setLoadingView(view).queueTo(call, false);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        PullRequest pullRequest = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), PullCommentActivity.class);
        intent.putExtra("owner", mOwner);
        intent.putExtra("repo", mRepo);
        intent.putExtra("number", pullRequest.number);
        intent.putExtra("pull_request", pullRequest);
        startActivity(intent);
    }
}
