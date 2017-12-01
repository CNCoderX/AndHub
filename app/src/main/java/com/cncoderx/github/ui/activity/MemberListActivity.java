package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.ui.adapter.UserListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.List;

import retrofit2.Call;

/**
 * @author cncoderx
 */
public class MemberListActivity extends SwipeRecyclerViewActivity implements OnItemClickListener {
    private UserListAdapter mAdapter = new UserListAdapter();
    private ListCallback<User> mCallback = new ListCallback<>(mAdapter);
    private String mOrg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrg = getIntent().getStringExtra(IntentExtra.KEY_USER);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_members));
        mCallback.setEmptyView(getEmptyView());

        getSwipeRefreshLayout().setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        User user = mAdapter.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(IntentExtra.KEY_USER, user.login);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        IUserService service = ServiceGenerator.create(IUserService.class);
        Call<List<User>> call = service.getOrganMembers(mOrg);
        mCallback.setRefreshView(getSwipeRefreshLayout()).queueTo(call, true);
    }
}
