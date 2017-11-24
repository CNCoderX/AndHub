package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.sdk.service.IRepositoryService;
import com.cncoderx.github.ui.activity.RepoDetailActivity;
import com.cncoderx.github.ui.adapter.RepoGridAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class MyReposFragment extends Fragment implements OnItemClickListener {
    @BindView(R.id.rv_my_repos_list)
    RecyclerView rvRepos;

    private RepoGridAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_repos, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new RepoGridAdapter();
        RecyclerViewHelper.setGridLayout(rvRepos, 2);
        RecyclerViewHelper.setSpace(rvRepos, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        RecyclerViewHelper.setAdapter(rvRepos, mAdapter);
        RecyclerViewHelper.setOnItemClickListener(rvRepos, this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        IRepositoryService service = ServiceGenerator.create(IRepositoryService.class);
        Call<List<Repository>> call = service.getRepositories();
        new ListCallback<>(mAdapter).queueTo(call);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Repository repository = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), RepoDetailActivity.class);
        intent.putExtra(IntentExtra.KEY_OWNER, repository.owner.login);
        intent.putExtra(IntentExtra.KEY_REPO, repository.name);
        startActivity(intent);
    }
}
