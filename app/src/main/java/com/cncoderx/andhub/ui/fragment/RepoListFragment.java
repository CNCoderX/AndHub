package com.cncoderx.andhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.Repository;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IRepositoryService;
import com.cncoderx.andhub.ui.activity.RepositoryActivity;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import io.reactivex.Single;

/**
 * @author cncoderx
 */
public class RepoListFragment extends RecyclerViewFragment implements OnItemClickListener {
    private BindingAdapter<Repository> mAdapter = new BindingAdapter<>(R.layout.item_repo_list, BR.repository);
    private Pagination<Repository> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<Repository>() {
        IRepositoryService mService = ServiceGenerator.create(IRepositoryService.class);

        @Override
        public Single<List<Repository>> create(int pageIndex, int pageSize) {
            return mService.getRepositories(mUser, pageIndex, pageSize);
        }
    });
    private String mUser;

    public static RepoListFragment create(String user) {
        RepoListFragment fragment = new RepoListFragment();
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
        setEmptyText(getString(R.string.no_repos));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<Repository>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), RepositoryActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        intent.putExtra(IntentExtra.KEY_REPO, repository.name);
        startActivity(intent);
    }
}
