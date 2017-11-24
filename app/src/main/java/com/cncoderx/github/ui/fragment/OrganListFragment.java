package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.ui.activity.OrganizationActivity;
import com.cncoderx.github.ui.adapter.UserListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class OrganListFragment extends SwipeRecyclerViewFragment implements OnItemClickListener {
    private UserListAdapter mAdapter = new UserListAdapter();
    private ListCallback<User> mCallback = new ListCallback<>(mAdapter, false);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_organs));
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSwipeRefreshLayout().setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        User organ = mAdapter.get(position);
        Intent intent = new Intent(getActivity(), OrganizationActivity.class);
        intent.putExtra(IntentExtra.KEY_ORGAN, organ.login);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        IUserService service = ServiceGenerator.create(IUserService.class);
        Call<List<User>> call = service.getOrgans();
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }
}
